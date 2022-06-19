package tw.evan_edmund.android_final_project

import android.content.Context
import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val albumlist = ArrayList<AlbumItem>()

        albumlist.add(AlbumItem("Music1", R.drawable.modifymeow))
        albumlist.add(AlbumItem("Music2", R.drawable.modifyteacher))
        albumlist.add(AlbumItem("Music3", R.drawable.modifymouse))
        albumlist.add(AlbumItem("Music4", R.drawable.modifycat_run1))
        albumlist.add(AlbumItem("Music5", R.drawable.modifycat_run2))

        val listView: ListView = findViewById(R.id.music_list)
        imgView = findViewById(R.id.img_top)

        listView.adapter = AlbumArrayAdapter(this, albumlist)

        listView.setOnItemClickListener{
                parent, view, position, id ->
            itemClicked(position)
        }

        /*music player*/
        btn_start = findViewById(R.id.start)
        btn_start.setOnClickListener { start() }
        btn_stop = findViewById(R.id.stop)
        btn_stop.setOnClickListener { stop() }
        btn_pause = findViewById(R.id.pause)
        btn_pause.setOnClickListener { pause() }

    }

    private fun itemClicked(position: Int){
        when (position){
            0->imgView.setImageResource(R.drawable.modifymeow)
            1-> imgView.setImageResource(R.drawable.modifyteacher)
            2-> imgView.setImageResource(R.drawable.modifymouse)
            3->imgView.setImageResource(R.drawable.modifycat_run1)
            4->imgView.setImageResource(R.drawable.modifycat_run2)
            else->{
                imgView.setImageResource(R.drawable.modifyax)
            }
        }
    }

    /*music function*/
    private fun start() {
        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        intent.putExtra("KEY_ISPAUSE", false);
        startService(intent)
    }

    private fun pause() {
        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        intent.putExtra("KEY_ISPAUSE", true)
        startService(intent)
    }

    private fun stop() {
        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        stopService(intent)
    }
}
class AlbumItem(val name: String, val imgId: Int) {

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
        tv_name.text = item!!.name
        val iv: ImageView = itemlayout?.findViewById(R.id.itemiv)!!
        iv.setImageResource(item.imgId)

        return itemlayout;

    }
}
