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
    lateinit var boss_img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fighting)

        level_tv = findViewById(R.id.level_num)
        your_blood_tv = findViewById(R.id.your_blood)
        boss_blood_tv = findViewById(R.id.boss_blood)

        your_img = findViewById(R.id.your_img)
        your_weapon = findViewById(R.id.your_weapon)
        your_hat = findViewById(R.id.your_hat)
        boss_img = findViewById(R.id.boss_img)

    }

    override fun onStart() {
        super.onStart()

        // context 類別內有 getSharedPreferences，可取得SharedPreferences的物件
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val my_level = pref.getInt(MainActivity.KEY_LEVEL, 0)
        val my_weapon = pref.getInt(MainActivity.KEY_WEAPON, -1)
        val my_hat = pref.getInt(MainActivity.KEY_HAT, -1)
        val my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General")
        val my_blood = pref.getInt(MainActivity.KEY_BLOOD, 100)
        val my_maxblood = pref.getInt(MainActivity.KEY_MAXBLOOD, 100)
        val my_points = pref.getInt(MainActivity.KEY_POINTS, 0)

        level_tv.setText(my_level.toString())

        if (my_weapon != -1){
            your_weapon.setImageResource(weapon_id_arr[my_weapon])
        }

        if (my_hat != -1){
            your_hat.setImageResource(hat_id_arr[my_hat])
        }

        your_blood_tv.setText("${my_blood}/${my_maxblood}")

        boss_img.setImageResource(R.drawable.modifymouse)
    }

    val img_id_arr = intArrayOf(R.drawable.modifycat_run1, R.drawable.modifycat_run2)
    val weapon_id_arr = intArrayOf(R.drawable.modifyax, R.drawable.modifygun)
    val hat_id_arr = intArrayOf(R.drawable.modifyhat,)
}
