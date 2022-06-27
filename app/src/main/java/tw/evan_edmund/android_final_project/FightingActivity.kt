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

    /*View*/
    lateinit var level_tv: TextView

    lateinit var boss_img_view: ImageView
    lateinit var boss_atk_tv:TextView
    lateinit var boss_blood_tv: TextView

    lateinit var user_blood_tv: TextView
    lateinit var user_atk_tv:TextView
    lateinit var user_img_view: ImageView
    lateinit var user_weapon_img_view: ImageView
    lateinit var user_hat_img_view: ImageView

    lateinit var pref: SharedPreferences
    lateinit var pref_edit: SharedPreferences.Editor
    lateinit var runnable: Runnable
    lateinit var handler: Handler
    /*----*/

    var index = 0

    //user
    var user_atk_num = 1
    var user_defense = 1.0
    var my_identity = "General"

    var my_weapon = -1
    var my_hat = -1
    var my_blood = 0
    var my_maxblood = 0

    //boss
    var current_boss_blood = 0
    var my_level = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fighting)

        pref = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        pref_edit = pref.edit()

        level_tv = findViewById(R.id.level_num)

        boss_img_view = findViewById(R.id.boss_img)
        boss_atk_tv = findViewById(R.id.boss_atk_tv)
        boss_blood_tv = findViewById(R.id.boss_blood)

        user_img_view = findViewById(R.id.your_img)
        user_atk_tv = findViewById(R.id.my_atk_tv)
        user_blood_tv = findViewById(R.id.your_blood)
        user_weapon_img_view = findViewById(R.id.your_weapon)
        user_hat_img_view = findViewById(R.id.your_hat)


        boss_img_view.setOnClickListener{
            user_hit_boss()
        }

        user_img_view.setOnClickListener{
            user_hit_user()
        }
    }

    override fun onStart() {
        // 初始話數值
        super.onStart()

        /*get database*/
        my_level = pref.getInt(MainActivity.KEY_LEVEL, 0)
        my_weapon = pref.getInt(MainActivity.KEY_WEAPON, -1)
        my_hat = pref.getInt(MainActivity.KEY_HAT, -1)
        my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General").toString()
        my_blood = pref.getInt(MainActivity.KEY_BLOOD, 100)
        my_maxblood = pref.getInt(MainActivity.KEY_MAXBLOOD, 100)

        /*Set weapon atk*/
        if (my_weapon != -1){
            user_weapon_img_view.setImageResource(weapon_id_arr[my_weapon])
            when (my_weapon){
                0 -> user_atk_num = 10
                1 -> user_atk_num = 50
                2 -> user_atk_num = 100
            }
        }

        /*Set hat defense*/
        if (my_hat != -1){
            user_hat_img_view.setImageResource(hat_id_arr[my_hat])
            when (my_hat){
                0 -> user_defense = 0.8
//                1 -> user_defense = 0.7
//                2 -> user_defense = 0.6
            }
        }

        level_tv.setText(my_level.toString())
        user_atk_tv.setText("ATK: ${user_atk_num}")
        user_blood_tv.setText("${my_blood}/${my_maxblood}")

        if(my_level < boss_id_arr.size){
            boss_img_view.setImageResource(boss_id_arr[my_level])
            boss_blood_tv.setText("${boss_blood[my_level]}/${boss_max_blood[my_level]}")
            current_boss_blood = boss_max_blood[my_level]
            boss_atk_tv.setText("ATK: ${boss_atk[my_level]}")

        }else{
            // boss 都挑戰完了
            boss_img_view.setImageResource(R.drawable.modifywhite)
            boss_img_view.setEnabled(false)
        }

        /*boss fight you*/
        boss_hit_user()

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    fun user_hit_boss(){
        current_boss_blood -= user_atk_num * 1
        boss_blood_tv.setText("${current_boss_blood}/${boss_max_blood[my_level]}")
        if (current_boss_blood <= 0){
            my_level += 1
            pref_edit.putInt(MainActivity.KEY_LEVEL, my_level)
            pref_edit.commit()

            Toast.makeText(this,
                "You're winner!", Toast.LENGTH_SHORT).show()
            Toast.makeText(this,
                "You obtained the boss's song.", Toast.LENGTH_SHORT).show()

            return_mainactivity()
        }
    }

    fun user_hit_user(){
        my_blood -= ((user_atk_num * 1).toDouble() * user_defense).toInt()
        user_blood_tv.setText("${my_blood}/${my_maxblood}")
        pref_edit.putInt(MainActivity.KEY_MAXBLOOD,my_blood)
        pref_edit.commit()

        if (my_blood <= 0){
            Toast.makeText(this,
                "Not enough blood.", Toast.LENGTH_SHORT).show()

            return_mainactivity()
        }
    }

    fun boss_hit_user(){
        handler = Handler(Looper.getMainLooper())

        runnable = Runnable{
            if((my_blood <= 0) or (current_boss_blood <= 0)){
                if (my_blood <= 0){
                    Toast.makeText(this,
                        "Not enough blood.", Toast.LENGTH_SHORT).show()
                    return_mainactivity()
                }
                handler.removeCallbacks(runnable) //stops a specific runnable
            }else{
                handler.postDelayed(runnable, 1000)
                my_blood -= Math.floor((boss_atk[my_level]).toDouble()*user_defense).toInt()
                user_blood_tv.setText("${my_blood}/${my_maxblood}")
                pref_edit.putInt(MainActivity.KEY_BLOOD, my_blood)
                pref_edit.commit()
            }
        }
        handler.post(runnable)
    }

    fun return_mainactivity(){
        var intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        setResult(RESULT_OK, intent);
        finish()
    }

    val img_id_arr = intArrayOf(R.drawable.modifycat_run1, R.drawable.modifycat_run2)
    val weapon_id_arr = intArrayOf(R.drawable.modifyax,R.drawable.modifyknife, R.drawable.modifygun)
    val hat_id_arr = intArrayOf(R.drawable.modifyhat,)
    val boss_id_arr =  intArrayOf(R.drawable.modifymeow,R.drawable.modifymouse,R.drawable.modifyteacher)
    val boss_max_blood = intArrayOf(50, 1000, 5000)
    val boss_blood = intArrayOf(50, 1000, 5000)
    val boss_atk = intArrayOf(1, 5, 10)
}
