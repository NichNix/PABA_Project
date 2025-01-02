import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import paba.project.berita_online.R
import paba.project.berita_online.database.NewsEntity // Import NewsEntity

class adapterBerita(private val listBerita: List<NewsEntity>) :
    RecyclerView.Adapter<adapterBerita.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: NewsEntity)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var judulBerita = itemView.findViewById<TextView>(R.id.tvJudul)
        var detailBerita = itemView.findViewById<TextView>(R.id.tvDetail)
        var gambarBerita = itemView.findViewById<ImageView>(R.id.gambarBerita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listBerita.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val berita = listBerita[position]

        holder.judulBerita.text = berita.title // Using NewsEntity's title field
        holder.detailBerita.text = berita.description // Using NewsEntity's content field

        // Assuming 'gambar' in NewsEntity is a URL, you can use Picasso to load it
        Picasso.get()
            .load(berita.imageUrl) // Assuming 'imageUrl' is the field name for the image in NewsEntity
            .into(holder.gambarBerita)

        holder.gambarBerita.setOnClickListener {
            onItemClickCallback.onItemClicked(berita)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}