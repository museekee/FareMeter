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
import android.location.LocationProvider
import android.os.Build
import android.os.Bundle
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
import java.util.Date


class LocationService : Service(), LocationListenerCompat {
    private lateinit var notification: Notification
    private val mNotificationId = 1
    private lateinit var locationManager: LocationManager
    private lateinit var pref: PrefManager
    private lateinit var fareCalcType: String
    private var transportation = unknownTransportation.id
    private lateinit var startTime: Date
    private var averageSpeed: Float = 0f
    private var topSpeed: Float = 0f

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!PermissionUtil.checkLocationPermission(this)) {
            PermissionUtil.openAppInfo(this)
            stopSelf()
        }
        startTime = Date()

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
                _id = 0,
                fareCalcType = fareCalcType,
                transportation = transportation,
                startTime = startTime,
                endTime = Date(),
                fare = MeterUtil.fare.value,
                averageSpeed = averageSpeed,
                topSpeed = topSpeed,
                distance = MeterUtil.distance.value
            )
        )
        MeterUtil.isDriving.value = false

        locationManager.removeUpdates(this)
        MeterUtil.resetValues()
        stopForeground(STOP_FOREGROUND_REMOVE)
        isRunning = false
    }
    override fun onLocationChanged(location: Location) {
        MeterUtil.increaseFare(location.speed)
        if (averageSpeed == 0f) averageSpeed = location.speed
        averageSpeed += location.speed
        averageSpeed /= 2

        if (location.speed > topSpeed)
            topSpeed = location.speed
        notiGosu(false)
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {
        MeterUtil.gpsStatus.value = when (status) {
            LocationProvider.OUT_OF_SERVICE -> GPSStatus.UNSTABLE
            LocationProvider.TEMPORARILY_UNAVAILABLE -> GPSStatus.UNSTABLE
            else -> GPSStatus.STABLE
        }
        super.onStatusChanged(provider, status, extras)
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
            .setContentText("운임: ${MeterUtil.fare.value}원 | 주행 거리: ${(MeterUtil.distance.value / 100).toInt() / 10}km | 속도: ${(MeterUtil.speed.value.toSpeedUnit(pref.speedUnit) * 10f).toInt() / 10f} km/h")
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