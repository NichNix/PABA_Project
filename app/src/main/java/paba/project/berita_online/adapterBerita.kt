import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import paba.project.berita_online.R
import paba.project.berita_online.database.NewsEntity

class adapterBerita(
    private val listBerita: List<NewsEntity>,
    private val sharedPref: SharedPreferences // Pass SharedPreferences to manage favorites
) : RecyclerView.Adapter<adapterBerita.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onDeleteClickCallback: OnDeleteClickCallback
    private lateinit var onFavoriteClickCallback: OnFavoriteClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: NewsEntity)
    }

    interface OnDeleteClickCallback {
        fun onDeleteClicked(data: NewsEntity)
    }

    interface OnFavoriteClickCallback {
        fun onFavoriteClicked(data: NewsEntity)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var judulBerita: TextView = itemView.findViewById(R.id.tvJudul)
        var detailBerita: TextView = itemView.findViewById(R.id.tvDetail)
        var gambarBerita: ImageView = itemView.findViewById(R.id.gambarBerita)
        var deleteButton: ImageButton = itemView.findViewById(R.id.delete_newsBtn)
        var favButton: ImageButton = itemView.findViewById(R.id.add_to_favoriteBtn) // Favorite button
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

        holder.judulBerita.text = berita.title
        holder.detailBerita.text = berita.description

        // Load image using Picasso
        Picasso.get()
            .load(berita.imageUrl)
            .into(holder.gambarBerita)

        // Set favorite button state based on SharedPreferences
        val isFavorite = sharedPref.getBoolean(berita.id.toString(), false)
        holder.favButton.setImageResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline)

        // Set onClickListener for the image (to navigate to detail)
        holder.gambarBerita.setOnClickListener {
            onItemClickCallback.onItemClicked(berita)
        }

        // Set onClickListener for the delete button
        holder.deleteButton.setOnClickListener {
            onDeleteClickCallback.onDeleteClicked(berita)
        }

        // Set onClickListener for the favorite button
        holder.favButton.setOnClickListener {
            onFavoriteClickCallback.onFavoriteClicked(berita)
            // Toggle favorite status and update button icon
            val isNowFavorite = !isFavorite
            sharedPref.edit().putBoolean(berita.id.toString(), isNowFavorite).apply()
            holder.favButton.setImageResource(if (isNowFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline)
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

    // Set the callback for favorite button click
    fun setOnFavoriteClickCallback(onFavoriteClickCallback: OnFavoriteClickCallback) {
        this.onFavoriteClickCallback = onFavoriteClickCallback
    }
}