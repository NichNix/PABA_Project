package paba.project.berita_online

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detailBerita : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val title = intent.getStringExtra("title")
//        val description = intent.getStringExtra("description")
//        val imageUrl = intent.getStringExtra("imageUrl")

//        val titleTextView = findViewById<TextView>(R.id.idJudul)
//        val descriptionTextView = findViewById<TextView>(R.id.idDetail)
//        val imageView = findViewById<ImageView>(R.id.idGambar)


//        var _idGambar = findViewById<ImageView>(R.id.idGambar)
//        var _idJudul = findViewById<TextView>(R.id.idJudul)
//        var _idDetail = findViewById<TextView>(R.id.idDetail)




    }
}