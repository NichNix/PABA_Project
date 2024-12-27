package paba.project.berita_online

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import paba.project.berita_online.database.AppDatabase
import paba.project.berita_online.database.UserDao

class profilPengguna : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var editProfileLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_pengguna)

        refreshProfileData()

        // Initialize the database and userDao
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "user-database"
        ).build()

        userDao = db.userDao()  // Initialize userDao from the database

        // Set up the ActivityResultLauncher for starting edit_profile activity
        editProfileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // If the result is OK, refresh the profile data
            if (result.resultCode == RESULT_OK) {
                refreshProfileData()
            }
        }

        // Find UI elements
        val editProfileButton = findViewById<Button>(R.id.editProfile_button)
        val backBtn = findViewById<ImageButton>(R.id.back_btn)

        // Set up click listeners
        editProfileButton.setOnClickListener {
            val intent = Intent(this, edit_profile::class.java)
            editProfileLauncher.launch(intent)  // Launch edit_profile and wait for the result
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Load profile data when the activity starts
        refreshProfileData()
    }

    // Method to refresh profile data from the database
    private fun refreshProfileData() {
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
                    emailText.text = email
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