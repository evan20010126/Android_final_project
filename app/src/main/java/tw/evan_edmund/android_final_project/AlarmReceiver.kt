package tw.evan_edmund.android_final_project

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

//        var text = intent.getStringExtra("TEXT").toString()

        val c: Calendar = Calendar.getInstance()
        c.setTimeInMillis(System.currentTimeMillis())
        val hour: Int = c.get(Calendar.HOUR_OF_DAY);
        val minute: Int = c.get(Calendar.MINUTE)

//        Log.w("yyy", "hour: ${hour}, minute: ${minute}")

        if(MainActivity.treasureDicision.open){
            val am = context.getSystemService(Context.ALARM_SERVICE) as?
                    AlarmManager
            val pi = Util.setPendingIntent(context)
            Util.setNextAlarm(context, am, pi, )
            Toast.makeText(context,R.string.next_treasure,Toast.LENGTH_LONG
            ).show()
            MainActivity.treasureDicision.open = false
            MainActivity.refresh.refresh = true
            MainActivity.first.first_enter_treasure = true
            MainActivity.treasureDicision.find = false
        }
        else{
            val am = context.getSystemService(Context.ALARM_SERVICE) as?
                    AlarmManager
            val pi = Util.setPendingIntent(context)
            Util.setNextAlarm(context, am, pi, )
            Toast.makeText(
                context, R.string.treasure_time,
                Toast.LENGTH_LONG
            ).show()
            var msg = "Go Find Your Treasure!!!"
            Util.sendNotification(context, msg!!)
            MainActivity.treasureDicision.open = true
            MainActivity.refresh.refresh = false
        }
    }
}