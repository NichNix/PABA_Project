package paba.project.berita_online.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import paba.project.berita_online.R
import paba.project.berita_online.database.UserEntity
import paba.project.berita_online.viewmodel.UserViewModel
import paba.project.berita_online.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val phoneNumber = binding.etPhoneNumber.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            }

            userViewModel.checkEmail(email) { isExist ->
                if (isExist) {
                    runOnUiThread {
                        Toast.makeText(this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val user = UserEntity(email = email, password = password, name = name, phoneNumber = phoneNumber)
                    userViewModel.registerUser(user) { isSuccess ->
                        runOnUiThread {
                            if (isSuccess) {
                                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                                finish() // Kembali ke halaman login
                            } else {
                                Toast.makeText(this, "Registrasi gagal, coba lagi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}
