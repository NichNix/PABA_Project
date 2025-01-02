package paba.project.berita_online

import adapterBerita
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

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

        // Check if the user is logged in
        val email = getUserSession()

        val greetingTextView: TextView = findViewById(R.id.tvGreeting)
        val loginButton: Button = findViewById(R.id.btnLogin)

        val _btnTambahBerita = findViewById<Button>(R.id.tambahBrt_btn)
        _btnTambahBerita.setOnClickListener {
            val intent = Intent(this, input_berita::class.java)
            startActivity(intent)
        }

        if (email == null) {
            // If no user session is found, navigate to LoginActivity
            loginButton.text = "Login" // Set text to "Login"
            loginButton.setOnClickListener {
                // Redirect to login page
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        } else {
            // If user is logged in, show a personalized greeting and logout button
            greetingTextView.text = "Hello, $email"
            loginButton.text = "Logout" // Set text to "Logout"
            loginButton.setOnClickListener {
                // Logout functionality: Clear user session
                clearUserSession()
                // Navigate back to LoginActivity
                navigateToLoginPage()
            }
        }

        val userProfileButton: Button = findViewById(R.id.btnUserprofile)
        userProfileButton.setOnClickListener {
            // Redirect to user profile page
            val intent = Intent(this, profilPengguna::class.java)
            startActivity(intent)
        }

        // Ensure system UI (status and navigation bars) are properly padded
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Update this method to display data from the NewsEntity database
    fun TampilkanData(newsList: List<NewsEntity>) {
        _rvBerita.layoutManager = LinearLayoutManager(this)

        val adBerita = adapterBerita(newsList) // Pass the list of news from the database
        _rvBerita.adapter = adBerita

        adBerita.setOnItemClickCallback(object : adapterBerita.OnItemClickCallback {
            override fun onItemClicked(data: NewsEntity) {
                Toast.makeText(this@MainActivity, data.title, Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(this@MainActivity, detailBerita::class.java)
                intent.putExtra("kirimData", data) // You can customize what you send
                startActivity(intent)
            }
        })
    }

    // Retrieve the user session (email) from SharedPreferences
    private fun getUserSession(): String? {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        return sharedPref.getString("user_email", null) // Return null if no session is found
    }

    // Clear the user session (logout)
    private fun clearUserSession() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("user_email") // Remove the stored email
        editor.apply() // Apply changes
    }

    // Navigate to LoginActivity
    private fun navigateToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close MainActivity so user can't go back
    }
}