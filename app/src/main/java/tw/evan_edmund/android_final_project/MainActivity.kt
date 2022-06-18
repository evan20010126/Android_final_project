package tw.evan_edmund.android_final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    companion object {
        val KEY_MONTH: String = "KEY_MONTH"
        val KEY_DAY: String = "KEY_DAY"
    }

    /* Button */
    lateinit var VIP_btn: Button
    lateinit var Treasure_btn: Button
    lateinit var Fight_btn: Button
    lateinit var Store_btn: Button

    /* TextView */
    lateinit var Identity_tv: TextView
    lateinit var Points_tv: TextView

    /*UI repeat*/
    lateinit var cat: ImageView
    lateinit var runnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VIP_btn = findViewById(R.id.VIP_btn)
        Treasure_btn = findViewById(R.id.Treasure_btn)
        Fight_btn = findViewById(R.id.Fight_btn)
        Store_btn = findViewById(R.id.Store_btn)

        Identity_tv = findViewById(R.id.Identity_tv)
        Points_tv = findViewById(R.id.Points_tv)

        var intent = Intent()
        VIP_btn.setOnClickListener{
            intent.setClass(this@MainActivity, VipActivity::class.java)
            this.startActivity(intent)
        }
        Treasure_btn.setOnClickListener{
            intent.setClass(this@MainActivity, TreasureActivity::class.java)
            this.startActivity(intent)
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

            handler.postDelayed(runnable, 1000)
        }

    }

    override fun onStart() {
        super.onStart()
        // 寫讀資料庫
    }
}