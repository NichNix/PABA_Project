package paba.project.berita_online

import adapterBerita
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import paba.project.berita_online.database.AppDatabase
import paba.project.berita_online.database.NewsDatabase
import paba.project.berita_online.database.NewsEntity
import paba.project.berita_online.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var _rvBerita: RecyclerView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences for managing favorites based on user session
        sharedPref = getUserFavoritesSharedPreferences()

        _rvBerita = findViewById<RecyclerView>(R.id.rvBerita)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "user-database"
        ).build()

        val newsDatabase = Room.databaseBuilder(
            applicationContext,
            NewsDatabase::class.java,
            "news-database"
        ).build()

        lifecycleScope.launch(Dispatchers.IO) {
            val newsDao = newsDatabase.newsDao()
            val allNews = newsDao.getAllNews() // Fetch all news items from the database
            Log.d("Debug", "All news in DB: $allNews")

            // Show the data in RecyclerView on the main thread
            launch(Dispatchers.Main) {
                if (allNews.isNotEmpty()) {
                    TampilkanData(allNews)
                }
            }
        }

        // User session management
        val email = getUserSession()

        val greetingTextView: TextView = findViewById(R.id.tvGreeting)
        val loginButton: Button = findViewById(R.id.btnLogin)

        val _btnTambahBerita = findViewById<Button>(R.id.tambahBrt_btn)
        _btnTambahBerita.setOnClickListener {
            val intent = Intent(this, input_berita::class.java)
            startActivity(intent)
        }

        if (email == null) {
            loginButton.text = "Login"
            loginButton.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        } else {
            greetingTextView.text = "Hello, $email"
            loginButton.text = "Logout"
            loginButton.setOnClickListener {
                clearUserSession()
                navigateToLoginPage()
            }
        }

        val userProfileButton: Button = findViewById(R.id.btnUserprofile)
        userProfileButton.setOnClickListener {
            val intent = Intent(this, profilPengguna::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to get user-specific SharedPreferences for favorites
    private fun getUserFavoritesSharedPreferences(): SharedPreferences {
        val email = getUserSession()
        return if (email != null) {
            getSharedPreferences("favorites_$email", Context.MODE_PRIVATE)
        } else {
            getSharedPreferences("favorites", Context.MODE_PRIVATE) // Default for non-logged-in users
        }
    }

    // Update this method to display data from the NewsEntity database
    fun TampilkanData(newsList: List<NewsEntity>) {
        _rvBerita.layoutManager = LinearLayoutManager(this)

        // Pass SharedPreferences when creating adapter
        val adBerita = adapterBerita(newsList, sharedPref) // Pass context and the list of news from the database
        _rvBerita.adapter = adBerita

        // Set the onItemClickCallback
        adBerita.setOnItemClickCallback(object : adapterBerita.OnItemClickCallback {
            override fun onItemClicked(data: NewsEntity) {
                Toast.makeText(this@MainActivity, data.title, Toast.LENGTH_LONG).show()
                val intent = Intent(this@MainActivity, detailBerita::class.java)
                intent.putExtra("kirimData", data) // Pass the NewsEntity as Parcelable
                startActivity(intent)
            }
        })

        // Set the onDeleteClickCallback to handle delete button click
        adBerita.setOnDeleteClickCallback(object : adapterBerita.OnDeleteClickCallback {
            override fun onDeleteClicked(data: NewsEntity) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val newsDao = NewsDatabase.getDatabase(applicationContext).newsDao()
                    newsDao.deleteNews(data) // Delete the selected news from the database

                    // Refresh the data after deletion
                    launch(Dispatchers.Main) {
                        val allNews = newsDao.getAllNews()
                        adBerita.notifyDataSetChanged()
                    }
                }
            }
        })

        // Set the favorite button click listener
        adBerita.setOnFavoriteClickCallback(object : adapterBerita.OnFavoriteClickCallback {
            override fun onFavoriteClicked(data: NewsEntity) {
                val isFavorite = sharedPref.getBoolean(data.id.toString(), false)
                val editor = sharedPref.edit()

                if (isFavorite) {
                    editor.remove(data.id.toString()) // Remove from favorites
                    Toast.makeText(this@MainActivity, "${data.title} removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    editor.putBoolean(data.id.toString(), true) // Add to favorites
                    Toast.makeText(this@MainActivity, "${data.title} added to favorites", Toast.LENGTH_SHORT).show()
                }
                editor.apply() // Save changes to SharedPreferences
                adBerita.notifyDataSetChanged() // Refresh adapter to update the icon
            }
        })
    }

    // Retrieve the user session (email) from SharedPreferences
    private fun getUserSession(): String? {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        return sharedPref.getString("user_email", null)
    }

    // Clear the user session (logout)
    private fun clearUserSession() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("user_email")
        editor.apply()
    }

    // Navigate to LoginActivity
    private fun navigateToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}