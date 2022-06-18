package tw.evan_edmund.android_final_project

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class FightingActivity : AppCompatActivity() {

    lateinit var level_tv: TextView
    lateinit var your_blood_tv: TextView
    lateinit var boss_blood_tv: TextView
    lateinit var your_img: ImageView
    lateinit var your_weapon: ImageView
    lateinit var your_hat: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fighting)



    }

    override fun onStart() {
        super.onStart()

        // context 類別內有 getSharedPreferences，可取得SharedPreferences的物件
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val my_level = pref.getInt(MainActivity.KEY_LEVEL, 1)
        val my_weapon = pref.getInt(MainActivity.KEY_WEAPON, -1)
        val my_hat = pref.getInt(MainActivity.KEY_HAT, -1)
        val my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General")
        val my_blood = pref.getInt(MainActivity.KEY_BLOOD, 100)
        val my_maxblood = pref.getInt(MainActivity.KEY_MAXBLOOD, 100)
        val my_points = pref.getInt(MainActivity.KEY_POINTS, 0)

    }
}