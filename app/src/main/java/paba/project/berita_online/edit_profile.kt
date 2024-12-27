package paba.project.berita_online

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import paba.project.berita_online.database.UserDao
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paba.project.berita_online.database.*

class edit_profile : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private var currentEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database").build()
        userDao = db.userDao()

        val namaPengguna = findViewById<TextView>(R.id.nama_lengkap)
        val nomorHp = findViewById<TextView>(R.id.nomor_hp)
        val emailUser = findViewById<TextView>(R.id.email_user)
        val password = findViewById<TextView>(R.id.password)
        val btnSimpan = findViewById<Button>(R.id.btn_simpan)

        currentEmail = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_email", null)

        lifecycleScope.launch(Dispatchers.IO) {
            currentEmail?.let { email ->
                userDao.getUserByEmail(email)?.let { user ->
                    withContext(Dispatchers.Main) {
                        namaPengguna.setText(user.name)
                        nomorHp.setText(user.phoneNumber)
                        emailUser.setText(user.email)
                        password.setText(user.password)
                    }
                }
            }
        }

        btnSimpan.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                currentEmail?.let { email ->
                    val updatedName = namaPengguna.text.toString()
                    val updatedPhone = nomorHp.text.toString()
                    val updatedPassword = password.text.toString()
                    val updatedEmail = emailUser.text.toString()

                    // Get the user by the current email
                    userDao.getUserByEmail(email)?.let { user ->
                        // Check if the fields have been changed and update them accordingly
                        if (user.name != updatedName) {
                            userDao.updateName(email, updatedName)
                        }

                        if (user.phoneNumber != updatedPhone) {
                            userDao.updatePhoneNumber(email, updatedPhone)
                        }

                        if (user.password != updatedPassword) {
                            userDao.updatePassword(email, updatedPassword)
                        }

                        // Handle email change separately (as it affects user session)
                        if (user.email != updatedEmail) {
                            userDao.updateEmail(email, updatedEmail)
                            withContext(Dispatchers.Main) {
                                getSharedPreferences("user_session", MODE_PRIVATE).edit()
                                    .putString("user_email", updatedEmail).apply()
                                currentEmail = updatedEmail // Update currentEmail reference
                            }
                        }

                        // Refresh UI by loading the updated user data
                        userDao.getUserByEmail(updatedEmail)?.let { updatedUser ->
                            withContext(Dispatchers.Main) {
                                namaPengguna.setText(updatedUser.name)
                                nomorHp.setText(updatedUser.phoneNumber)
                                emailUser.setText(updatedUser.email)
                                password.setText(updatedUser.password)

                                // Notify the user that the profile has been updated
                                Toast.makeText(this@edit_profile, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}