package tw.evan_edmund.android_final_project

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random
class TreasureActivity : AppCompatActivity() , LocationListener {

    lateinit var locmgr: LocationManager
    var current_latitude: String = ""
    var current_longitude: String = ""
    var current_altitude: String = ""
    var current_distance: String = "Loading..."
    var my_identity: String = ""
    lateinit var goal: Location
    var treasure_latitude: Double = 0.0
    var treasure_longitude: Double = 0.0
    lateinit var runnable: Runnable
    lateinit var nav_btn : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w("hihi","hihi")

        setContentView(R.layout.activity_treasure)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                1)
        } else {
//            initLoc()
        }
        nav_btn = findViewById(R.id.nav_btn)
        nav_btn.setOnClickListener{
            checkVIP()
        }
//        initLoc()
    }

    override fun onStart() {
        super.onStart()

        initLoc()

        Log.w("onstarthihi","onstarthihi2")


        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General").toString()
        var handler = Handler(Looper.getMainLooper())

        runnable = Runnable{
            if(MainActivity.refresh.refresh == true){


//                this.startActivity(intent_refresh)
                Toast.makeText(this, "Treasure time is over", Toast.LENGTH_SHORT).show()
//                onDestroy()
                var intent_refresh = Intent()
                intent_refresh.setClass(this, MainActivity::class.java)
                this.setResult(RESULT_OK,intent_refresh)
                finish()
            }
            else {
                handler.postDelayed(runnable, 500)
            }
        }

        handler.post(runnable)
    }
    private fun checkVIP(){
        if(my_identity=="VIP"){
            navigation()
        }
        else{
            Toast.makeText(this, "Sorry, you're not VIP", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode,
            permissions, grantResults)

        if ((grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED)) {
//            initLoc()
        }
    }
    private fun initLoc() {
        locmgr = getSystemService(LOCATION_SERVICE) as
                LocationManager

        var loc: Location? = null
        try {

            loc = locmgr.getLastKnownLocation(
                LocationManager.GPS_PROVIDER)
        } catch (e: SecurityException) {
            Log.w("eee1: ", "${e}")
        }

        if (loc != null) {
            current_latitude = loc.latitude.toString()
            current_longitude = loc.longitude.toString()
            current_altitude = loc.altitude.toString()
            showLocation()
        } else {
            Toast.makeText(this, "Can't get position", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            locmgr.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, 1f, this)
        } catch (e: SecurityException) {
            Log.w("eee2: ", "${e}")
        }
        if(MainActivity.first.first_enter_treasure) {
            MainActivity.first.first_enter_treasure = false
            MainActivity.treasure_position.rand_x = Random.nextDouble(from = -0.003, until = 0.003)
            MainActivity.treasure_position.rand_y = Random.nextDouble(from = -0.003, until = 0.003)
        }
//        Log.w("randx: ", "${rand_x}")
//        Log.w("randy: ","${rand_y}")
        MainActivity.treasure_position.latitude = current_latitude.toDouble() + MainActivity.treasure_position.rand_x
        MainActivity.treasure_position.longitude = current_longitude.toDouble() + MainActivity.treasure_position.rand_y
        goal = Location("goal")
        goal.setLatitude(MainActivity.treasure_position.latitude)
        goal.setLongitude(MainActivity.treasure_position.longitude)
        if (loc != null) {
            current_distance = loc.distanceTo(goal).toString()
        }
//        Log.w("treatureX: ","${treasure_latitude}")
//        Log.w("treatureY: ","${treasure_longitude}")
        var textView_treasure = findViewById<TextView>(R.id.treasurePosition)
        val str_treature = String.format("%.7f",MainActivity.treasure_position.latitude) + ", " + String.format("%.7f",MainActivity.treasure_position.longitude)
        textView_treasure.text = str_treature
    }

    override fun onDestroy() {
        super.onDestroy()
        locmgr.removeUpdates(this)
    }

    override fun onLocationChanged(loc: Location) {

        current_distance = loc.distanceTo(goal).toString()
        current_latitude = loc.latitude.toString()
        current_longitude = loc.longitude.toString()
        current_altitude = loc.altitude.toString()
        showLocation()
        Toast.makeText(
            this, "Location has changed", Toast.LENGTH_SHORT
        ).show()
    }
    private fun showLocation(){
        var textView_current = findViewById<TextView>(R.id.currentPosition)
        var textView_distance = findViewById<TextView>(R.id.distance)
        val str_current = String.format("%.7f",current_latitude.toDouble()) + ", " + String.format("%.7f",current_longitude.toDouble())
        textView_current.text = str_current
        if(current_distance=="Loading..."){
            textView_distance.setText(R.string.loading)
            return
        }
        textView_distance.text = "${current_distance} m"
        if(current_distance.toDouble() <= 50.0){
            Toast.makeText(this, "Congratulation! You Earn 100 Points", Toast.LENGTH_SHORT).show()
            val pref: SharedPreferences = this.getSharedPreferences(
                MainActivity.XMLFILE,
                AppCompatActivity.MODE_PRIVATE
            )
            val pref_edit = pref.edit()
            var my_points = pref.getInt(MainActivity.KEY_POINTS, 1000) + 100
            pref_edit.putInt(MainActivity.KEY_POINTS, my_points)
            pref_edit.commit()

            var intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            setResult(RESULT_OK, intent);
            finish()
        }
    }
    private fun navigation() {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("http://maps.google.com/maps?" +
                "saddr=${current_latitude},${current_longitude}&" +
                "daddr=${treasure_latitude},${treasure_longitude}")
        startActivity(intent)
    }
    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

}

