package tw.evan_edmund.android_final_project

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.Preference
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher

class FightingActivity : AppCompatActivity() {

    lateinit var level_tv: TextView
    lateinit var your_blood_tv: TextView
    lateinit var boss_blood_tv: TextView

    lateinit var boss_atk_tv:TextView
    lateinit var your_atk_tv:TextView

    lateinit var your_img: ImageView
    lateinit var your_weapon: ImageView
    lateinit var your_hat: ImageView
    lateinit var boss_img: ImageView

    lateinit var pref: SharedPreferences
    lateinit var runnable: Runnable
    var index = 0

    //user
    var user_atk_num = 1
    var user_defense = 1.0

    //boss
    var current_boss_blood = 0
    var my_level = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fighting)

        // context 類別內有 getSharedPreferences，可取得SharedPreferences的物件
        pref = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )

        level_tv = findViewById(R.id.level_num)
        your_blood_tv = findViewById(R.id.your_blood)
        boss_blood_tv = findViewById(R.id.boss_blood)

        your_atk_tv = findViewById(R.id.my_atk_tv)
        boss_atk_tv = findViewById(R.id.boss_atk_tv)

        your_img = findViewById(R.id.your_img)
        your_weapon = findViewById(R.id.your_weapon)
        your_hat = findViewById(R.id.your_hat)
        boss_img = findViewById(R.id.boss_img)

        boss_img.setOnClickListener{
            current_boss_blood -= user_atk_num * 1
            boss_blood_tv.setText("${current_boss_blood}/${boss_max_blood[my_level]}")
            if (current_boss_blood <= 0){
                var pref_edit = pref.edit()
                my_level += 1
                pref_edit.putInt(MainActivity.KEY_LEVEL, my_level)
                pref_edit.commit()

                Toast.makeText(this,
                    "You're winner!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this,
                    "You obtained the boss's song.", Toast.LENGTH_SHORT).show()

                var intent = Intent()
                intent.setClass(this, MainActivity::class.java)
                setResult(RESULT_OK, intent);
                finish()
            }
        }

    }

    override fun onStart() {
        // 初始話數值
        super.onStart()

        my_level = pref.getInt(MainActivity.KEY_LEVEL, 0)
        val my_weapon = pref.getInt(MainActivity.KEY_WEAPON, -1)
        val my_hat = pref.getInt(MainActivity.KEY_HAT, -1)
        val my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General")
        var my_blood = pref.getInt(MainActivity.KEY_BLOOD, 100)
        val my_maxblood = pref.getInt(MainActivity.KEY_MAXBLOOD, 100)
        val my_points = pref.getInt(MainActivity.KEY_POINTS, 0)

        var pref_edit = pref.edit()

        level_tv.setText(my_level.toString())

        if (my_weapon != -1){
            your_weapon.setImageResource(weapon_id_arr[my_weapon])
            when (my_weapon){
                0 -> user_atk_num = 10
                1 -> user_atk_num = 50
                2 -> user_atk_num = 100
            }
        }

        your_atk_tv.setText("ATK: ${user_atk_num}")

        if (my_hat != -1){
            your_hat.setImageResource(hat_id_arr[my_hat])
            when (my_hat){
                0 -> user_defense = 0.8
//                1 -> user_defense = 0.7
//                2 -> user_defense = 0.6
            }
        }

        your_blood_tv.setText("${my_blood}/${my_maxblood}")

        if(my_level < 3){
            // 三個魔王
            boss_img.setImageResource(boss_id_arr[my_level])
            boss_blood_tv.setText("${boss_blood[my_level]}/${boss_max_blood[my_level]}")
            current_boss_blood = boss_max_blood[my_level]
            boss_atk_tv.setText("ATK: ${boss_atk[my_level]}")

        }else{
            // game over!
        }


        /*boss fight you*/
        var handler = Handler(Looper.getMainLooper())

        runnable = Runnable{
            if((my_blood <= 0) or (current_boss_blood <= 0)){
                if (my_blood <= 0){
                    Toast.makeText(this,
                            "Not enough blood.", Toast.LENGTH_SHORT).show()

                    var intent = Intent()
                    intent.setClass(this, MainActivity::class.java)
                    setResult(RESULT_OK, intent);
                    finish()
                }
                handler.removeCallbacks(runnable) //stops a specific runnable
            }else{
                handler.postDelayed(runnable, 1000)
                my_blood -= Math.floor((boss_atk[my_level]).toDouble()*user_defense).toInt()
                your_blood_tv.setText("${my_blood}/${my_maxblood}")
                pref_edit.putInt(MainActivity.KEY_BLOOD, my_blood)
                pref_edit.commit()
            }
        }
        handler.post(runnable)

    }

    val img_id_arr = intArrayOf(R.drawable.modifycat_run1, R.drawable.modifycat_run2)
    val weapon_id_arr = intArrayOf(R.drawable.modifyax, R.drawable.modifygun)
    val hat_id_arr = intArrayOf(R.drawable.modifyhat,)
    val boss_id_arr =  intArrayOf(R.drawable.modifymeow,R.drawable.modifymouse,R.drawable.modifyteacher)
    val boss_max_blood = intArrayOf(50, 2000, 50000)
    val boss_blood = intArrayOf(50, 2000, 50000)
    val boss_atk = intArrayOf(10, 50, 100)
}
