package tw.evan_edmund.android_final_project

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder

class MusicService : Service() {

//    override fun onBind(intent: Intent): IBinder {
//        TODO("Return the communication channel to the service.")
//    }

    companion object {
        val CHANNEL_ID = "evan_edmund.musicplayer"
        val NOTIFICATION_ID = 1
    }

    lateinit var player: MediaPlayer
    lateinit var pi: PendingIntent

    override fun onBind(intent: Intent): IBinder {
        val binder = Binder()
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        // 建立 mediaplayer物件
        player = MediaPlayer()

        val uri: Uri = Uri.parse("android.resource://" +
                getPackageName() + "/raw/dontdownloadthissong")

        player?.setDataSource(this, uri);
        player?.setOnCompletionListener {
            try {
                player?.stop()
                player?.prepare()
            } catch (e: Exception) {
            }
        }

        player?.prepare() //準備撥音樂 .start即可開始

        val intent = Intent()
        intent.setClass(this,
            MainActivity::class.java)

        pi = PendingIntent.getActivity(this,
            0, intent, PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE)

        var notification: Notification? = null
        try {
            notification = getNotification(this, pi,
                getString(R.string.app_name), "Don\'t Download This Song")
        } catch (e: Exception) {
        }
        if (notification != null) {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val isPause = intent?.getBooleanExtra(
            "KEY_ISPAUSE", true);

        if (isPause == true) {
            if(!player.isPlaying ) {
                return Service.START_STICKY
            }
            try {
                player.pause();
            } catch (e: Exception) {
            }
        } else {
            try {
                player.start();
            } catch (e: Exception) {
            }
        }

        var status: String = "(暫停中)"
        if (player.isPlaying) {
            status = "(播放中)"
        }

        var notification: Notification? = null
        try {
            notification = getNotification(this, pi,
                getString(R.string.app_name), "Don\'t Download This Song " + status)
        } catch (e: Exception) {
        }
        if (notification != null) {
            startForeground(NOTIFICATION_ID, notification)
        }

        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            player.stop()
            player.release()
        } catch (e: Exception) {
        }
        stopForeground(true)
    }

    private fun getNotification(context: Context, pi: PendingIntent,
                                title: String, msg: String): Notification? {

        var notification: Notification? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channel = NotificationChannel(CHANNEL_ID,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW)
                channel.setShowBadge(false)
                val notificationManager: NotificationManager =
                    context.getSystemService(NotificationManager::class.java)
                notificationManager!!.createNotificationChannel(channel)
                notification = Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pi)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(msg)
                    .setWhen(System.currentTimeMillis())
                    .build()
            } else if (Build.VERSION.SDK_INT >= 16){
                notification = Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pi)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(msg)
                    .setWhen(System.currentTimeMillis())
                    .build()
            }
        } catch (throwable: Throwable) {
            return null
        }
        return notification
    }
}