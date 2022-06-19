package tw.evan_edmund.android_final_project

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.core.graphics.scaleMatrix

class MainActivity : AppCompatActivity() {

    object treasureDicision{
        var open: Boolean = false
    }
    object VIP_check{
        var is_vip = false
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

    override fun onStart() {
        super.onStart()

        // context 類別內有 getSharedPreferences，可取得SharedPreferences的物件
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        ) // android內部會用一個檔案XMLFILE.xml(用xml檔存)來儲存要儲存的data
        // 第一個參數: xml的主檔名
        // 以前有Context.MODE_WORLD_READABLE: 整個手機的其他app都可以讀但危險，所以之後沒得選只能選Context.MODE_PRIVATE

        // sharepreference 為 key value的概念
        val my_level = pref.getInt(MainActivity.KEY_LEVEL, 0)
        val my_weapon = pref.getInt(MainActivity.KEY_WEAPON, -1)
        val my_hat = pref.getInt(MainActivity.KEY_HAT, -1)
        val my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General")
        val my_blood = pref.getInt(MainActivity.KEY_BLOOD, 100)
        val my_maxblood = pref.getInt(MainActivity.KEY_MAXBLOOD, 100)
        val my_points = pref.getInt(MainActivity.KEY_POINTS, 0)

        if (my_weapon != -1) {
            weapon_imgview.setImageResource(weapon_id_arr[my_weapon])
        }
        if (my_hat != -1){
            hat_imgview.setImageResource(hat_id_arr[my_hat])
        }

        if (my_identity == "General"){
            Identity_tv.setText(R.string.General)
        }else if(my_identity == "VIP"){
            Identity_tv.setText(R.string.VIP)
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
            intent.setClass(this@MainActivity, TreasureActivity::class.java)
            this.startActivity(intent)
        }
        else{
            Toast.makeText(this, "Treasure is not ready", Toast.LENGTH_SHORT).show()
        }
    }
}

val img_id_arr = intArrayOf(R.drawable.modifycat_run1, R.drawable.modifycat_run2)
val weapon_id_arr = intArrayOf(R.drawable.modifyax, R.drawable.modifygun)
val hat_id_arr = intArrayOf(R.drawable.modifyhat,)