package paba.project.berita_online

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paba.project.berita_online.database.AppDatabase
import paba.project.berita_online.database.UserDao

class profilPengguna : AppCompatActivity() {

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_pengguna)

        // Initialize the database and userDao
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "user-database"
        ).build()

        val _editProfileButton = findViewById<Button>(R.id.editProfile_button)
        _editProfileButton.setOnClickListener {
            val intent = Intent(this, edit_profile::class.java)
            startActivity(intent)
        }

        userDao = db.userDao()  // Initialize userDao from the database

        val namaPengguna = findViewById<TextView>(R.id.nama_lengkap)
        val nomorHp = findViewById<TextView>(R.id.nomor_hp)
        val emailText = findViewById<TextView>(R.id.email)

        // Get the logged-in user's email from SharedPreferences
        val email = getUserSession()

        if (email == null) {
            // No user logged in, show a message or handle accordingly
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show()
        } else {
            // Fetch user details based on the email
            lifecycleScope.launch {
                try {
                    emailText.text = "$email"
                    val user = userDao.getUserByEmail(email)  // Fetch user by email
                    if (user != null) {
                        // If user found, update UI
                        namaPengguna.text = user.name
                        nomorHp.text = user.phoneNumber
                    } else {
                        // User not found, handle accordingly
                        Toast.makeText(this@profilPengguna, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("profilPengguna", "Error fetching user by email: ${e.message}")
                }
            }
        }
    }

    // Retrieve the user session (email) from SharedPreferences
    private fun getUserSession(): String? {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val email = sharedPref.getString("user_email", null) // Retrieve the email from SharedPreferences
        Log.d("profilPengguna", "Email retrieved from session: $email")  // Log the retrieved email
        return email
    }
}