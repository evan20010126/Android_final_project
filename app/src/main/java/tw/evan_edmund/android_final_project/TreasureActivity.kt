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
    var current_distance: String = ""
    var my_identity: String = ""
    lateinit var goal: Location
    var treasure_latitude: Double = 0.0
    var treasure_longitude: Double = 0.0
    lateinit var runnable: Runnable
    lateinit var nav_btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            initLoc()
        }
        nav_btn = findViewById(R.id.nav_btn)
        nav_btn.setOnClickListener{
            checkVIP()
        }
        initLoc()
    }

    override fun onStart() {
        super.onStart()
        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )
        my_identity = pref.getString(MainActivity.KEY_IDENTITY, "General").toString()
        var handler = Handler(Looper.getMainLooper())

        runnable = Runnable{
            if(MainActivity.refresh.refresh == true){
                var intent_refresh = Intent()
                intent_refresh.setClass(this, MainActivity::class.java)
                this.startActivity(intent_refresh)
                Toast.makeText(this, "Treasure time is over", Toast.LENGTH_SHORT).show()
                onDestroy()
            }
            handler.postDelayed(runnable, 500)
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
            initLoc()
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
        }

        if (loc != null) {
            current_latitude = loc.latitude.toString()
            current_longitude = loc.longitude.toString()
            current_altitude = loc.altitude.toString()
            showLocation()
        } else {
            Toast.makeText(this, "Can't get position", Toast.LENGTH_SHORT).show()
        }

        try {
            locmgr.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, 1f, this)
        } catch (e: SecurityException) {
        }
        var rand_x = Random.nextDouble(from=-0.003, until=0.003)
        var rand_y = Random.nextDouble(from=-0.003, until=0.003)
        Log.w("randx: ", "${rand_x}")
        Log.w("randy: ","${rand_y}")
        treasure_latitude = current_latitude.toDouble() + rand_x
        treasure_longitude = current_longitude.toDouble() + rand_y
        goal = Location("goal")
        goal.setLatitude(treasure_latitude)
        goal.setLongitude(treasure_longitude)
        if (loc != null) {
            current_distance = loc.distanceTo(goal).toString()
        }
        var textView_treasure = findViewById<TextView>(R.id.treasurePosition)
        val str_treature = String.format("%.7f",treasure_latitude) + ", " + String.format("%.7f",treasure_longitude)
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
        textView_distance.text = "$current_distance m"
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

