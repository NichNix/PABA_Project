package paba.project.berita_online.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import paba.project.berita_online.MainActivity
import paba.project.berita_online.R
import paba.project.berita_online.databinding.ActivityLoginBinding
import paba.project.berita_online.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Perform login using ViewModel
            userViewModel.loginUser(email, password) { user ->
                if (user != null) {
                    // Save user email to SharedPreferences
                    val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("user_email", user.email)  // Save the user's email
                    editor.apply()

                    Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    navigateToMainPage()
                } else {
                    Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            navigateToRegisterPage()
        }
    }

    private fun navigateToMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToRegisterPage() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
