package tw.evan_edmund.android_final_project

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class StoreActivity : AppCompatActivity() {
    lateinit var cola_btn: Button
    lateinit var bread_btn: Button
    lateinit var hat_btn: Button
    lateinit var ax_btn: Button
    lateinit var knife_btn: Button
    lateinit var gun_btn: Button
    lateinit var hp_num: TextView
    lateinit var point_num: TextView
    var my_weapon: Int = -1
    var my_hat: Int = -1
    var my_blood: Int = 100
    var my_maxblood: Int = 100
    var my_points: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        cola_btn = findViewById(R.id.buycola)
        bread_btn = findViewById(R.id.buybread)
        hat_btn = findViewById(R.id.buyhat)
        ax_btn = findViewById(R.id.buyax)
        knife_btn = findViewById(R.id.buyknife)
        gun_btn = findViewById(R.id.buygun)
        hp_num = findViewById(R.id.hp_num)
        point_num = findViewById(R.id.Points_tv)

        cola_btn.setOnClickListener {
            clickCola()
        }
        bread_btn.setOnClickListener {
            clickBread()
        }
        hat_btn.setOnClickListener {
            clickHat()
        }
        ax_btn.setOnClickListener {
            clickAx()
        }
        knife_btn.setOnClickListener {
            clickKnife()
        }
        gun_btn.setOnClickListener {
            clickGun()
        }
    }
    override fun onStart() {
        super.onStart()
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        ) // android內部會用一個檔案XMLFILE.xml(用xml檔存)來儲存要儲存的data
        my_weapon = pref.getInt(MainActivity.KEY_WEAPON, -1)
        my_hat = pref.getInt(MainActivity.KEY_HAT, -1)
        my_blood = pref.getInt(MainActivity.KEY_BLOOD, 100)
        my_maxblood = pref.getInt(MainActivity.KEY_MAXBLOOD, 100)
        my_points = pref.getInt(MainActivity.KEY_POINTS, 1000)
        val has_hat = pref.getInt(MainActivity.KEY_HAS_HAT, 0)
        val has_ax = pref.getInt(MainActivity.KEY_HAS_AX, 0)
        val has_knife = pref.getInt(MainActivity.KEY_HAS_KNIFE, 0)
        val has_gun = pref.getInt(MainActivity.KEY_HAS_GUN, 0)

        hp_num.setText("${my_blood}/${my_maxblood}")
        point_num.setText(("${my_points}$"))

        if(has_hat == 1){
            hat_btn.setText("0$")
        }
        if(has_ax == 1){
            ax_btn.setText("0$")
        }
        if(has_knife == 1){
            knife_btn.setText("0$")
        }
        if(has_gun == 1){
            gun_btn.setText("0$")
        }
    }
    private fun clickCola(){
        var s = cola_btn.getText().toString().subSequence(0,cola_btn.getText().length-1)
        var cost = s.toString().toInt()
        if(my_points<cost){
            Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            return
        }
        if(my_blood>=my_maxblood){
            Toast.makeText(this, "Your HP is Full", Toast.LENGTH_SHORT).show()
            return
        }
//        Log.w("cost: ", "${cost}")
        my_blood += 10
        my_points -= cost
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val pref_edit = pref.edit()
        pref_edit.putInt(MainActivity.KEY_BLOOD, my_blood)
        pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
        pref_edit.commit()
        hp_num.setText("${my_blood}/${my_maxblood}")
        point_num.setText(("${my_points}$"))
    }
    private fun clickBread(){
        var s = bread_btn.getText().toString().subSequence(0,bread_btn.getText().length-1)
        var cost = s.toString().toInt()
        if(my_points<cost){
            Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            return
        }
        my_maxblood += 50
        my_points -= cost
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val pref_edit = pref.edit()
        pref_edit.putInt(MainActivity.KEY_BLOOD, my_blood)
        pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
        pref_edit.commit()
        hp_num.setText("${my_blood}/${my_maxblood}")
        point_num.setText(("${my_points}$"))
    }
    private fun clickHat(){
        var s = hat_btn.getText().toString().subSequence(0,hat_btn.getText().length-1)
        var cost = s.toString().toInt()
        if(my_points<cost){
            Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            return
        }
        my_points -= cost
        if(my_hat == 0) {
            Toast.makeText(this, "Take off the hat!", Toast.LENGTH_SHORT).show()
            my_hat = -1
        }
        else if(my_hat == -1) {
            Toast.makeText(this, "Wear the hat!", Toast.LENGTH_SHORT).show()
            my_hat = 0
        }
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val pref_edit = pref.edit()
        pref_edit.putInt(MainActivity.KEY_HAS_HAT, 1)
        pref_edit.putInt(MainActivity.KEY_HAT, my_hat)
        pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
        pref_edit.commit()
        hat_btn.setText("0$")
        point_num.setText(("${my_points}$"))
    }
    private fun clickAx(){

        var s = ax_btn.getText().toString().subSequence(0,ax_btn.getText().length-1)
        var cost = s.toString().toInt()
        if(my_points<cost){
            Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Change the weapon!", Toast.LENGTH_SHORT).show()
        my_points -= cost
        if(my_weapon != 0)
            my_weapon = 0
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val pref_edit = pref.edit()
        pref_edit.putInt(MainActivity.KEY_HAS_AX, 1)
        pref_edit.putInt(MainActivity.KEY_WEAPON, my_weapon)
        pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
        pref_edit.commit()
        ax_btn.setText("0$")
        point_num.setText(("${my_points}$"))
    }
    private fun clickKnife(){

        var s = ax_btn.getText().toString().subSequence(0,ax_btn.getText().length-1)
        var cost = s.toString().toInt()
        if(my_points<cost){
            Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Change the weapon!", Toast.LENGTH_SHORT).show()
        my_points -= cost
        if(my_weapon != 2)
            my_weapon = 2
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val pref_edit = pref.edit()
        pref_edit.putInt(MainActivity.KEY_HAS_KNIFE, 1)
        pref_edit.putInt(MainActivity.KEY_WEAPON, my_weapon)
        pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
        pref_edit.commit()
        knife_btn.setText("0$")
        point_num.setText(("${my_points}$"))
    }
    private fun clickGun(){
        var s = gun_btn.getText().toString().subSequence(0,gun_btn.getText().length-1)
        var cost = s.toString().toInt()
        if(my_points<cost){
            Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Change the weapon!", Toast.LENGTH_SHORT).show()
        my_points -= cost
        if(my_weapon != 1)
            my_weapon = 1
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        val pref_edit = pref.edit()
        pref_edit.putInt(MainActivity.KEY_HAS_GUN, 1)
        pref_edit.putInt(MainActivity.KEY_WEAPON, my_weapon)
        pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
        pref_edit.commit()
        gun_btn.setText("0$")
        point_num.setText(("${my_points}$"))
    }
}