package tw.evan_edmund.android_final_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MusicActivity : AppCompatActivity() {

    lateinit var imgView: ImageView

    lateinit var btn_start: Button
    lateinit var btn_stop: Button
    lateinit var btn_pause: Button

    var state = "stop"
    var my_level = 0

    object current_select_music
    {
        var select_music: Int = -1
        var current_playing: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val albumlist = ArrayList<AlbumItem>()

        albumlist.add(AlbumItem(R.string.Nyancat, R.drawable.modifymeow))
        albumlist.add(AlbumItem(R.string.Mouse, R.drawable.modifymouse))
        albumlist.add(AlbumItem(R.string.Teacher, R.drawable.modifyteacher))
//        albumlist.add(AlbumItem("Music4", R.drawable.modifycat_run1))
//        albumlist.add(AlbumItem("Music5", R.drawable.modifycat_run2))

        val listView: ListView = findViewById(R.id.music_list)
        imgView = findViewById(R.id.img_top)

        listView.adapter = AlbumArrayAdapter(this, albumlist)

        listView.setOnItemClickListener{
                parent, view, position, id ->
            itemClicked(position)
        }

        /*music player*/
        btn_start = findViewById(R.id.start)
        btn_start.setOnClickListener { start(my_level) }
        btn_stop = findViewById(R.id.stop)
        btn_stop.setOnClickListener { stop() }
        btn_pause = findViewById(R.id.pause)
        btn_pause.setOnClickListener { pause() }

        imgView.setImageResource(R.drawable.modifywhite)
        btn_pause.setEnabled(false);
        btn_start.setEnabled(false);
        btn_stop.setEnabled(false);
    }

    override fun onStart() {
        super.onStart()

        val pref: SharedPreferences = this.getSharedPreferences(
            MainActivity.XMLFILE,
            AppCompatActivity.MODE_PRIVATE
        )

        my_level = pref.getInt(MainActivity.KEY_LEVEL, 0)


    }
    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    private fun itemClicked(position: Int){
        when (position){
            0-> imgView.setImageResource(R.drawable.modifymeow)
            1-> imgView.setImageResource(R.drawable.modifymouse)
            2-> imgView.setImageResource(R.drawable.modifyteacher)
//            3->imgView.setImageResource(R.drawable.modifycat_run1)
//            4->imgView.setImageResource(R.drawable.modifycat_run2)
            else->{
                imgView.setImageResource(R.drawable.modifyax)
            }
        }
        current_select_music.select_music = position
        if (current_select_music.current_playing == -1 ){ //??????????????????
            btn_start.setEnabled(true) //??????????????????start
        }else{
            if (current_select_music.current_playing == current_select_music.select_music){
                if(state == "start"){
                    btn_pause.setEnabled(true);
                    btn_start.setEnabled(false);
                    btn_stop.setEnabled(true);
                }else if(state == "pause"){
                    btn_pause.setEnabled(false);
                    btn_start.setEnabled(true);
                    btn_stop.setEnabled(true);
                }else{
                    btn_pause.setEnabled(false);
                    btn_start.setEnabled(true);
                    btn_stop.setEnabled(false);
                }
            }else{
                if (state != "stop"){
                    btn_pause.setEnabled(false);
                    btn_start.setEnabled(false);
                    btn_stop.setEnabled(true);
                }else{
                    btn_pause.setEnabled(false);
                    btn_start.setEnabled(true);
                    btn_stop.setEnabled(false);
                }
            }
        }

    }

    /*music function*/
    private fun start(level:Int) {
        if (current_select_music.select_music < level){
            MusicActivity.current_select_music.current_playing = MusicActivity.current_select_music.select_music

            state = "start"
            btn_pause.setEnabled(true);
            btn_start.setEnabled(false);
            btn_stop.setEnabled(true);

            val intent = Intent()
            intent.setClass(this, MusicService::class.java)
            intent.putExtra("KEY_ISPAUSE", false);
            startService(intent)
        }else{
            Toast.makeText(this,R.string.Not_obtained, Toast.LENGTH_LONG).show()
        }

    }

    private fun pause() {
        state = "pause"
        btn_pause.setEnabled(false);
        btn_start.setEnabled(true);
        btn_stop.setEnabled(true);

        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        intent.putExtra("KEY_ISPAUSE", true)
        startService(intent)
    }

    private fun stop() {
        state = "stop"
        btn_pause.setEnabled(false);
        btn_start.setEnabled(true);
        btn_stop.setEnabled(false);

        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        stopService(intent)
    }
}
class AlbumItem(val name: Int, val imgId: Int) {

}
class AlbumArrayAdapter(val c: Context, val items: ArrayList<AlbumItem>) :
    ArrayAdapter<AlbumItem>(c, 0, items) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(c)

        var itemlayout: LinearLayout? = null

        if (convertView == null) {
            itemlayout = inflater.inflate(R.layout.listitem, null)
                    as? LinearLayout
        } else {
            itemlayout = convertView as? LinearLayout
        }

        val item: AlbumItem? = getItem(position) as? AlbumItem

        val tv_name: TextView = itemlayout?.findViewById(R.id.itemtv)!!

//        tv_name.text = item!!.name
        tv_name.setText(item!!.name)

        val iv: ImageView = itemlayout?.findViewById(R.id.itemiv)!!
        iv.setImageResource(item.imgId)

        return itemlayout;

    }
}
