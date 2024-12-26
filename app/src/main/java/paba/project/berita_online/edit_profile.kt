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
                val updatedUser = UserEntity(
                    email = emailUser.text.toString(),
                    password = password.text.toString(),
                    name = namaPengguna.text.toString(),
                    phoneNumber = nomorHp.text.toString()
                )
                userDao.insertUser(updatedUser)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@edit_profile, "Profile updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}