package paba.project.berita_online

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class detailBerita : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_berita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _idGambar = findViewById<ImageView>(R.id.idGambar)
        val _idJudul = findViewById<TextView>(R.id.idJudul)
        val _idDetail = findViewById<TextView>(R.id.idDetail)

        val dataIntent = intent.getParcelableExtra<berita>("kirimData",
            berita::class.java)
        if (dataIntent != null) {
            Picasso.get()
                .load(dataIntent.gambar)
                .into(_idGambar)
            _idJudul.setText(dataIntent.judul)
            _idDetail.setText(dataIntent.detail)
        }
    }
}