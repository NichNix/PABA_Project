import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import paba.project.berita_online.R
import paba.project.berita_online.database.NewsEntity // Import NewsEntity

class adapterBerita(private val listBerita: List<NewsEntity>) :
    RecyclerView.Adapter<adapterBerita.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onDeleteClickCallback: OnDeleteClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: NewsEntity)
    }

    interface OnDeleteClickCallback {
        fun onDeleteClicked(data: NewsEntity)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var judulBerita = itemView.findViewById<TextView>(R.id.tvJudul)
        var detailBerita = itemView.findViewById<TextView>(R.id.tvDetail)
        var gambarBerita = itemView.findViewById<ImageView>(R.id.gambarBerita)
        var deleteButton = itemView.findViewById<ImageButton>(R.id.delete_newsBtn) // Delete button
        var add_to_favButton = itemView.findViewById<ImageButton>(R.id.add_to_favoriteBtn) // Favorite button
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

        // Load image using Picasso
        Picasso.get()
            .load(berita.imageUrl)
            .into(holder.gambarBerita)

        // Set onClickListener for the image (to navigate to detail)
        holder.gambarBerita.setOnClickListener {
            onItemClickCallback.onItemClicked(berita)
        }

        // Set onClickListener for the delete button
        holder.deleteButton.setOnClickListener {
            onDeleteClickCallback.onDeleteClicked(berita)
        }
    }

    // Set the callback for item click
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // Set the callback for delete button click
    fun setOnDeleteClickCallback(onDeleteClickCallback: OnDeleteClickCallback) {
        this.onDeleteClickCallback = onDeleteClickCallback
    }
}