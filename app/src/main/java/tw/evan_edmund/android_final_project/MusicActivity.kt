package tw.evan_edmund.android_final_project

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MusicActivity : AppCompatActivity() {
    lateinit var imgView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val albumlist = ArrayList<AlbumItem>()

        albumlist.add(AlbumItem("Music1", R.drawable.modifyax))
        albumlist.add(AlbumItem("Music2", R.drawable.modifygun))
        albumlist.add(AlbumItem("Music3", R.drawable.modifyhat))
        albumlist.add(AlbumItem("Music4", R.drawable.modifycat_run1))
        albumlist.add(AlbumItem("Music5", R.drawable.modifycat_run2))

        val listView: ListView = findViewById(R.id.music_list)
        imgView = findViewById(R.id.img_top)

        listView.adapter = AlbumArrayAdapter(this, albumlist)

        listView.setOnItemClickListener{
                parent, view, position, id ->
            itemClicked(position)
        }
    }
    private fun itemClicked(position: Int){
        when (position){
            0->imgView.setImageResource(R.drawable.modifyax)
            1-> imgView.setImageResource(R.drawable.modifygun)
            2-> imgView.setImageResource(R.drawable.modifyhat)
            3->imgView.setImageResource(R.drawable.modifycat_run1)
            4->imgView.setImageResource(R.drawable.modifycat_run2)
            else->{
                imgView.setImageResource(R.drawable.modifyax)
            }
        }
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
