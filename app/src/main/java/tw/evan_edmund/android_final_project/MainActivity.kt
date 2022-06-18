package tw.evan_edmund.android_final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.scaleMatrix

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
    lateinit var cat_imgview: ImageView
    lateinit var runnable: Runnable
    var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VIP_btn = findViewById(R.id.VIP_btn)
        Treasure_btn = findViewById(R.id.Treasure_btn)
        Fight_btn = findViewById(R.id.Fight_btn)
        Store_btn = findViewById(R.id.Store_btn)

        Identity_tv = findViewById(R.id.Identity_tv)
        Points_tv = findViewById(R.id.Points_tv)

        cat_imgview = findViewById(R.id.cat_imgview)

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
            cat_imgview.setImageResource(img_id_arr[index%2])
            index++
            if (index%10 == 0) {
                cat_imgview.scaleX *= -1
            }
            handler.postDelayed(runnable, 500)
        }

        handler.post(runnable)
    }

    override fun onStart() {
        super.onStart()
        // 寫讀資料庫
    }
}

val img_id_arr = intArrayOf(R.drawable.cat_run1, R.drawable.cat_run2)
