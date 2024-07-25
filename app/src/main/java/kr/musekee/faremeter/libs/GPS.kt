package kr.musekee.faremeter.libs

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kr.musekee.faremeter.ACTION_STOP
import kr.musekee.faremeter.MainActivity
import kr.musekee.faremeter.R


class LocationService(private val context: Context) : Service(), LocationListener {
    private var location: Location? = null
    private var speed = 0.0f

    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    init {
        requestLocationUpdates()
    }

    private var mBinder: IBinder = MyBinder()


    internal class MyBinder : Binder() {
        private val service: LocationService
            get() =// 서비스 객체를 리턴
                this@MyBinder.service
    }


    override fun onLocationChanged(location: Location) {
        Log.d("SERVICEDA", speed.toString())
        speed = location.speed
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SERVICEDA", "ONSTARTCOMMAND")
        if (intent?.action != null && intent.action.equals(
                ACTION_STOP, ignoreCase = true)) {
            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()
        }
        generateForegroundNotification()
        return START_STICKY
    }

    fun requestLocationUpdates() {
        try {
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            Log.d("SERVICEDA", "$isGPSEnabled, $isNetworkEnabled")
            // GPS_PROVIDER를 사용하여 위치 업데이트 요청
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES, // 위치 업데이트 간격(ms)
                MIN_DISTANCE_CHANGE_UPDATES.toFloat(),   // 최소 거리(m)
                this
            )
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: SecurityException) {
            e.printStackTrace()
            // 위치 권한이 없는 경우 처리
        }
    }

    fun getSpeed(): Float {
        return speed
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    fun stopUsingGps() {
        locationManager.removeUpdates(this@LocationService)
    }

    private fun generateForegroundNotification() {
        val intentMainLanding = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intentMainLanding, PendingIntent.FLAG_IMMUTABLE)
        iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        if (mNotificationManager == null) {
            mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        assert(mNotificationManager != null)
        mNotificationManager?.createNotificationChannelGroup(
            NotificationChannelGroup("chats_group", "Chats")
        )
        val notificationChannel =
            NotificationChannel("service_channel", "Service Notifications",
                NotificationManager.IMPORTANCE_MIN)
        notificationChannel.enableLights(false)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
        mNotificationManager?.createNotificationChannel(notificationChannel)
        val builder = NotificationCompat.Builder(this, "service_channel")

        builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append(" service is running").toString())
            .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
            .setContentText("Touch to open") //                    , swipe down for more options.
            .setSmallIcon(R.drawable.ic_taxi)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setWhen(0)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
        if (iconNotification != null) {
            builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
        }
        builder.color = resources.getColor(R.color.purple_200)
        notification = builder.build()
        startForeground(mNotificationId, notification)
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_UPDATES: Long = 1
        private const val MIN_TIME_BW_UPDATES: Long = 10 // 1000ms = 1sec
    }
}