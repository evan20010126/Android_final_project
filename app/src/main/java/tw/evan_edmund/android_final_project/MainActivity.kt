package tw.evan_edmund.android_final_project

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.core.graphics.scaleMatrix

class MainActivity : AppCompatActivity() {

    object treasureDicision{
        var open: Boolean = false
    }
    object VIP_check{
        var is_vip = false
    }
    object refresh{
        var refresh = false
    }
    companion object {
        val XMLFILE: String = "GAME_DATA"
        val KEY_IDENTITY: String = "KEY_IDENTITY"
        val KEY_POINTS: String = "KEY_POINTS"
        val KEY_MAXBLOOD: String = "KEY_MAXBLOOD"
        val KEY_BLOOD: String = "KEY_BLOOD"

        val KEY_HAT: String = "KEY_HAT"
        val KEY_WEAPON: String = "KEY_WEAPON"
        val KEY_LEVEL: String = "KEY_LEVEL"

        val KEY_HAS_AX: String = "KEY_HAS_AX"
        val KEY_HAS_KNIFE: String = "KEY_HAS_KNIFE"
        val KEY_HAS_GUN: String = "KEY_HAS_GUN"
        val KEY_HAS_HAT: String = "KEY_HAS_HAT"
    }

    /* Button */
    lateinit var VIP_btn: Button
    lateinit var Music_btn: ImageButton
    lateinit var Treasure_btn: Button
    lateinit var Fight_btn: Button
    lateinit var Store_btn: Button

    /* TextView */
    lateinit var Identity_tv: TextView
    lateinit var Points_tv: TextView
    lateinit var Hp_tv: TextView

    lateinit var pref_edit: SharedPreferences.Editor
    lateinit var pref: SharedPreferences

    /*UI repeat*/
    lateinit var cat_imgview: ImageView
    lateinit var hat_imgview: ImageView
    lateinit var weapon_imgview: ImageView
    lateinit var runnable: Runnable
    var index = 0

    var pendingintent: PendingIntent? = null
    var am: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VIP_btn = findViewById(R.id.VIP_btn)
        Music_btn = findViewById(R.id.music_btn)
        Treasure_btn = findViewById(R.id.Treasure_btn)
        Fight_btn = findViewById(R.id.Fight_btn)
        Store_btn = findViewById(R.id.Store_btn)

        Identity_tv = findViewById(R.id.Identity_tv)
        Points_tv = findViewById(R.id.Points_tv)
        Hp_tv = findViewById(R.id.hp)

        cat_imgview = findViewById(R.id.cat_imgview)
        hat_imgview = findViewById(R.id.hat_imgview)
        weapon_imgview = findViewById(R.id.weapon_imgview)

        pendingintent = Util.setPendingIntent(this@MainActivity)
        am = getSystemService(Context.ALARM_SERVICE) as?
                AlarmManager?
        Util.setNextAlarm(this, am, pendingintent)
        Toast.makeText(this, "Set Treasure time",
            Toast.LENGTH_SHORT).show();

        // context 類別內有 getSharedPreferences，可取得SharedPreferences的物件
        pref = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        ) // android內部會用一個檔案XMLFILE.xml(用xml檔存)來儲存要儲存的data
        // 第一個參數: xml的主檔名
        // 以前有Context.MODE_WORLD_READABLE: 整個手機的其他app都可以讀但危險，所以之後沒得選只能選Context.MODE_PRIVATE

        // sharepreference 為 key value的概念


        pref_edit = pref.edit()
        /**test remove all data**/ // will no storage last time data
        pref_edit.clear().commit()


        var intent = Intent()
        VIP_btn.setOnClickListener{
            intent.setClass(this@MainActivity, VipActivity::class.java)
            this.startActivity(intent)
        }
        Music_btn.setOnClickListener{
            intent.setClass(this@MainActivity, MusicActivity::class.java)
            this.startActivity(intent)
        }
        Treasure_btn.setOnClickListener{
            treasureFunction()
        }
        Fight_btn.setOnClickListener{
            intent.setClass(this@MainActivity, FightingActivity::class.java)
            this.startActivity(intent)
        }
        Store_btn.setOnClickListener{
            intent.setClass(this@MainActivity, StoreActivity::class.java)
            this.startActivity(intent)
        }

        var handler = Handler(Looper.getMainLooper())

        runnable = Runnable{
            cat_imgview.setImageResource(img_id_arr[index%2])
            index++
            if (index%10 == 0) {
                cat_imgview.scaleX *= -1
                weapon_imgview.scaleX *= -1
                hat_imgview.scaleX *= -1
            }
            handler.postDelayed(runnable, 500)
        }

        handler.post(runnable)

    }

    @SuppressLint("ResourceAsColor")
    override fun onStart() {
        super.onStart()

        var my_level = pref.getInt(MainActivity.KEY_LEVEL, 0)
        var my_weapon = pref.getInt(MainActivity.KEY_WEAPON, -1)
        var my_hat = pref.getInt(MainActivity.KEY_HAT, -1)
        var my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General")
        var my_blood = pref.getInt(MainActivity.KEY_BLOOD, 100)
        var my_maxblood = pref.getInt(MainActivity.KEY_MAXBLOOD, 100)
        var my_points = pref.getInt(MainActivity.KEY_POINTS, 1000)

        /** test version **/
//        my_level = 3
//        my_identity = "VIP"
//        my_blood = 99999
//        my_maxblood = 99999
//        my_points = 99999999

        pref_edit.putInt(MainActivity.KEY_LEVEL, my_level)
        pref_edit.putInt(MainActivity.KEY_WEAPON, my_weapon)
        pref_edit.putInt(MainActivity.KEY_HAT, my_hat)
        pref_edit.putString(MainActivity.KEY_IDENTITY, my_identity)
        pref_edit.putInt(MainActivity.KEY_BLOOD, my_blood)
        pref_edit.putInt(MainActivity.KEY_MAXBLOOD, my_maxblood)
        pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
        pref_edit.commit()


        if (my_weapon != -1) {
            weapon_imgview.setImageResource(weapon_id_arr[my_weapon])
        }
        if (my_hat != -1){
            hat_imgview.setImageResource(hat_id_arr[my_hat])
        }

        if (my_identity == "General"){
            Identity_tv.setText(R.string.General)
            VIP_check.is_vip = false
            VIP_btn.setEnabled(true)

        }else if(my_identity == "VIP"){
            Identity_tv.setText(R.string.VIP)
            VIP_check.is_vip = true
            VIP_btn.setEnabled(false)
        }
        Points_tv.setText(my_points.toString())
        Hp_tv.setText("${my_blood.toString()}/${my_maxblood.toString()}")

        //        Identity_tv.setText(R.string.VIP)

        // 取資料，第二個參數為default value(若是第一次執行 一開始根本沒有此xml檔 so根本沒有KEY_MONTH的key 會用default value)
        //        etMonth?.setText(pref_month) // 把資料放在edittext上
        //        val intDay = pref.getInt(KEY_DAY, 1);
        //        etDay?.setText("" + intDay);
    }
    private fun treasureFunction(){
        if(treasureDicision.open) {
//            intent.setClass(this@MainActivity, TreasureActivity::class.java)
            var intent_T = Intent()
            intent_T.setClass(this, TreasureActivity::class.java)
            this.setResult(RESULT_OK, intent_T)
            finish()
        }
        else{
            Toast.makeText(this, "Treasure is not ready", Toast.LENGTH_SHORT).show()
        }
    }
}

val img_id_arr = intArrayOf(R.drawable.modifycat_run1, R.drawable.modifycat_run2)
val weapon_id_arr = intArrayOf(R.drawable.modifyax,R.drawable.modifyax, R.drawable.modifygun)
val hat_id_arr = intArrayOf(R.drawable.modifyhat,)