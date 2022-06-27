package tw.evan_edmund.android_final_project

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Util{

    companion object {
        var textFromMain:String? = null
        val TEXT = "TEXT"
        val CHANNEL_ID = "evanedmund.implicitbroadcast"

        fun setPendingIntent(context: Context): PendingIntent {

//            textFromMain = text
//            Log.w("passmsg", "string:${textFromMain}")

            var intent: Intent = Intent()
            intent.setClass(context, AlarmReceiver::class.java)
//            intent.putExtra(TEXT, textFromMain)
            // context.sendBroadcast(intent)

            val pendingintent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT
                        or PendingIntent.FLAG_IMMUTABLE)
            return pendingintent
        }

        fun setNextAlarm(context: Context, am: AlarmManager?,
                         pi: PendingIntent?) {

            var c: Calendar = Calendar.getInstance()
            c.setTimeInMillis(System.currentTimeMillis())

            c.add(Calendar.MINUTE,2)

            c.set(Calendar.SECOND, 0)
            if (Build.VERSION.SDK_INT >= 31) {
                if (am?.canScheduleExactAlarms() == true) {

                    am?.setExact(
                        AlarmManager.RTC_WAKEUP,
                        c.timeInMillis, pi)

                } else {
                    am?.set(AlarmManager.RTC_WAKEUP, c.timeInMillis, pi)
                }
            } else {
                am?.setExact(
                    AlarmManager.RTC_WAKEUP,
                    c.timeInMillis, pi)
            }
            var msg = ""
            if(c.get(Calendar.MINUTE) < 10){
                msg = "Next Treasure Time-> " + c.get(Calendar.HOUR_OF_DAY) + " : 0" + c.get(Calendar.MINUTE)
            }
            else{
                msg = "Next Treasure Time-> " + c.get(Calendar.HOUR_OF_DAY) + " : " + c.get(Calendar.MINUTE)
            }


            sendNotification(context, msg!!)
        }

        fun sendNotification(context: Context, msg: String) {
            val intent = Intent()
            intent.setClass(context, MainActivity::class.java)
            intent.putExtra("FROMNOTIFICATION", 1)
            val pi = PendingIntent.getActivity(context,
                0, intent, PendingIntent.FLAG_MUTABLE)
            var notification: Notification? = null
            try {
                notification = getNotification(context, pi,
                    context.getString(R.string.app_name), msg)
            } catch (e: Exception) {
            }
            if (notification != null) {
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE)
                            as NotificationManager
                notificationManager.notify(1, notification)
            }
        }

        fun getNotification(context: Context, pi: PendingIntent,
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
                    notificationManager.createNotificationChannel(channel)
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
}