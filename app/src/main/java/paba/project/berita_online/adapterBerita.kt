package paba.project.berita_online

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class adapterBerita (private val listBerita: ArrayList<berita>) : RecyclerView
    .Adapter<adapterBerita.ListViewHolder> () {

        private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data:berita)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _judulBerita = itemView.findViewById<TextView>(R.id.tvJudul)
        var _detailBerita = itemView.findViewById<TextView>(R.id.tvDetail)
        var _gambarBerita = itemView.findViewById<ImageView>(R.id.gambarBerita)
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
        var berita = listBerita[position]

        holder._judulBerita.setText(berita.judul)
        holder._detailBerita.setText(berita.detail)
        Log.d("TEST", berita.gambar)
        Picasso.get()
            .load(berita.gambar)
            .into(holder._gambarBerita)

        holder._gambarBerita.setOnClickListener {
            onItemClickCallback.onItemClicked(listBerita[position])
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}