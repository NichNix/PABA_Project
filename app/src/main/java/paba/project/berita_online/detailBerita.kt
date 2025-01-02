package paba.project.berita_online

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import paba.project.berita_online.database.NewsDatabase
import paba.project.berita_online.database.NewsEntity

class detailBerita : AppCompatActivity() {
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

        // Get the news ID from the intent
        val newsId = intent.getIntExtra("newsId", -1)
        Log.d("DetailBerita", "News ID: $newsId")

        // Check if valid ID is passed
        if (newsId != -1) {
            // Fetch the news data from the database
            lifecycleScope.launch(Dispatchers.IO) {
                val newsDao = NewsDatabase.getDatabase(applicationContext).newsDao()
                val newsData = newsDao.getNewsById(newsId) // Retrieve the full news by ID

                // Log data to see if it exists
                Log.d("DetailBerita", "News data: $newsData")

                // Update UI on the main thread
                launch(Dispatchers.Main) {
                    if (newsData != null) {
                        Log.d("DetailBerita", "Loading news data: ${newsData.title}")
                        // Load image using Picasso
                        Picasso.get()
                            .load(newsData.imageUrl)
                            .placeholder(R.drawable.placeholder) // Add a placeholder image
                            .error(R.drawable.error_image) // Add an error image
                            .into(_idGambar)
                        _idJudul.text = newsData.title
                        _idDetail.text = newsData.description
                    } else {
                        Log.d("DetailBerita", "No data found for newsId: $newsId")
                    }
                }
            }
        }
    }
}