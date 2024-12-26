package paba.project.berita_online.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import paba.project.berita_online.database.AppDatabase
import paba.project.berita_online.database.UserEntity

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun registerUser(user: UserEntity, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                userDao.insertUser(user)
                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun loginUser(email: String, password: String, onResult: (UserEntity?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userDao.getUser(email, password)
                onResult(user)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }

    fun checkEmail(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userDao.getUserByEmail(email)
                onResult(user != null)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
}
