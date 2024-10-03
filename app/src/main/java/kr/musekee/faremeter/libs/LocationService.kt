package kr.musekee.faremeter.libs

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.location.LocationListenerCompat
import kr.musekee.faremeter.MainActivity
import kr.musekee.faremeter.NOTI_CHANNEL
import kr.musekee.faremeter.R
import kr.musekee.faremeter.activities.TaxiActivity
import kr.musekee.faremeter.datas.taxi
import kr.musekee.faremeter.datas.unknownTransportation
import java.security.MessageDigest


class LocationService : Service(), LocationListenerCompat {
    private lateinit var ID: String
    private val drivingDataDao = DrivingDataDao(DatabaseHelper(this))

    private lateinit var notification: Notification
    private val mNotificationId = 1

    private lateinit var locationManager: LocationManager
    private lateinit var pref: PrefManager
    private lateinit var fareCalcType: String

    private var transportation = unknownTransportation.id

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate() {
        super.onCreate()
        //region ID 만들기
        val md = MessageDigest.getInstance("SHA1")
        md.update(System.currentTimeMillis().toString().toByteArray(Charsets.UTF_8))
        ID = md.digest().toHexString()
        //endregion

        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!PermissionUtil.checkLocationPermission(this)) {
            PermissionUtil.openAppInfo(this)
            stopSelf()
        }

        pref = PrefManager(this)
        transportation =  pref.transportation
        fareCalcType = pref.getCalcType(transportation)
        MeterUtil.gpsStatus.value = GPSStatus.UNSTABLE

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_UPDATES,
            this
        )
        MeterUtil.isDriving.value = true
        notiGosu(true)
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()
        RecordDao(DatabaseHelper(this)).saveData(
            RecordData(
                id = ID,
                fareCalcType = fareCalcType,
                transportation = transportation,
                endTime = System.currentTimeMillis(),
                fare = MeterUtil.fare.value,
                distance = MeterUtil.distance.value
            )
        )
        MeterUtil.isDriving.value = false

        locationManager.removeUpdates(this)
        MeterUtil.resetValues()
        stopForeground(STOP_FOREGROUND_REMOVE)
        isRunning = false
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        if (provider == LocationManager.GPS_PROVIDER)
            MeterUtil.gpsStatus.value = GPSStatus.UNSTABLE
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        if (provider == LocationManager.GPS_PROVIDER)
            MeterUtil.gpsStatus.value = GPSStatus.STABLE   
    }

    override fun onLocationChanged(location: Location) {
        drivingDataDao.addData(DrivingData(
            id = ID,
            latitude = location.latitude,
            longitude = location.longitude,
            speed = location.speed.toDouble(),
            time = System.currentTimeMillis()
        ))
        MeterUtil.increaseFare(location.speed)
        notiGosu(false)
    }

    fun stopUsingGps() {
        locationManager.removeUpdates(this@LocationService)
    }

    private fun notiGosu(isFirst: Boolean) {
        val notiIntent = if (transportation == taxi.id)
            Intent(this, TaxiActivity::class.java)

        else Intent(this, MainActivity::class.java)
        notiIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK

        createNotificationChannel()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notiIntent,
            PendingIntent.FLAG_IMMUTABLE

        )

        val notiBuilder = NotificationCompat.Builder(this, NOTI_CHANNEL)
            .setContentTitle("대중교통 미터기 실행중")
            .setContentText("운임: ${MeterUtil.fare.value.wonFormat()}원 | 주행 거리: ${(MeterUtil.distance.value / 100).toInt() / 10}km | 속도: ${(MeterUtil.speed.value.toSpeedUnit(pref.speedUnit) * 10f).toInt() / 10f} km/h")
            .setSmallIcon(R.drawable.ic_bus)
            .setContentIntent(pendingIntent)
        notification = notiBuilder.build()

        @RequiresApi(Build.VERSION_CODES.S)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notiBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
        }

        if(isFirst) {
            startForeground(mNotificationId, notification)
        } else {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(mNotificationId, notification)
        }
    }

    private fun createNotificationChannel() {
        val name = "대중교통 미터기 실행중"
        val descriptionText = "대중교통 미터기 알림이 실행중입니다."
        val importance = NotificationManager.IMPORTANCE_MIN
        val channel = NotificationChannel(NOTI_CHANNEL, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val MIN_TIME_BW_UPDATES: Long = 300 // 1000ms = 1sec
        private const val MIN_DISTANCE_CHANGE_UPDATES: Float = 0f
        var isRunning = false
    }
}