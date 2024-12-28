package paba.project.berita_online

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import paba.project.berita_online.R
import paba.project.berita_online.database.AppDatabase
import paba.project.berita_online.database.NewsDatabase
import paba.project.berita_online.database.NewsEntity

class input_berita : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_input_berita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _judul = findViewById<EditText>(R.id.etTitle)
        val _detail = findViewById<EditText>(R.id.detail_berita)
        val _gambar = findViewById<EditText>(R.id.link_gambar)

        val database = NewsDatabase.getDatabase(this)

        val btnSave = findViewById<Button>(R.id.save_btn)
        btnSave.setOnClickListener {
            val title = _judul.text.toString()
            val detail = _detail.text.toString()
            val imageUrl = _gambar.text.toString()

            if (title.isNotEmpty() && detail.isNotEmpty()) {
                lifecycleScope.launch {
                    val newsEntity = NewsEntity(
                        title = title,
                        description = detail,
                        imageUrl = imageUrl
                    )
                    database.newsDao().insertNews(newsEntity)
                }
            }
            finish()
        }
    }



}