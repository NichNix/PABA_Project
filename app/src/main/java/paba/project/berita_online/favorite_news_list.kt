package paba.project.berita_online

import adapterBerita
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import paba.project.berita_online.database.NewsDatabase
import paba.project.berita_online.database.NewsEntity

class favorite_news_list : AppCompatActivity() {

    private lateinit var rvFavoriteNews: RecyclerView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_news_list)

        rvFavoriteNews = findViewById(R.id.fav_newsList) // Reference your RecyclerView
        sharedPref = getSharedPreferences("favorite_news", MODE_PRIVATE) // Access SharedPreferences


        // Apply padding for system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load favorite news from database
        loadFavoriteNews()
    }

    private fun loadFavoriteNews() {
        // Get the user email from the session (logged-in user)
        val email = getUserSession()

        if (email == null) {
            // Handle case when user is not logged in (optional)
            return
        }

        // Access user-specific SharedPreferences for favorites
        sharedPref = getSharedPreferences("favorites_$email", Context.MODE_PRIVATE)

        // Get the list of favorite news IDs from SharedPreferences
        val favoriteNewsIds = sharedPref.all.filterValues { it == true }.keys.map { it.toInt() }

        // If no favorite news, stop here
        if (favoriteNewsIds.isEmpty()) {
            return
        }

        // Access the Room database and query the news that matches the favorite IDs
        val newsDatabase = Room.databaseBuilder(
            applicationContext,
            NewsDatabase::class.java,
            "news-database"
        ).build()

        lifecycleScope.launch(Dispatchers.IO) {
            val newsDao = newsDatabase.newsDao()
            // Fetch the favorite news by their IDs
            val favoriteNewsList = newsDao.getNewsByIds(favoriteNewsIds)

            // Display the favorite news on the main thread
            launch(Dispatchers.Main) {
                displayFavoriteNews(favoriteNewsList)
            }
        }
    }

    // Function to display favorite news in the RecyclerView
    private fun displayFavoriteNews(favoriteNewsList: List<NewsEntity>) {
        rvFavoriteNews.layoutManager = LinearLayoutManager(this)
        val adBerita = adapterBerita(favoriteNewsList, sharedPref) // Use the same adapter with favorites functionality
        rvFavoriteNews.adapter = adBerita
    }

    // Function to get the user email from SharedPreferences (the session information)
    private fun getUserSession(): String? {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        return sharedPref.getString("user_email", null) // Replace "user_email" with the key used for storing email in SharedPreferences
    }
}