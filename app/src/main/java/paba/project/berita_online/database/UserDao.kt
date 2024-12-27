package paba.project.berita_online.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface UserDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    // Update name by email
    @Query("UPDATE users SET name = :name WHERE email = :email")
    suspend fun updateName(email: String, name: String)

    // Update phone number by email
    @Query("UPDATE users SET phoneNumber = :phoneNumber WHERE email = :email")
    suspend fun updatePhoneNumber(email: String, phoneNumber: String)

    // Update password by email
    @Query("UPDATE users SET password = :password WHERE email = :email")
    suspend fun updatePassword(email: String, password: String)

    // Update email by current email (change email)
    @Query("UPDATE users SET email = :newEmail WHERE email = :currentEmail")
    suspend fun updateEmail(currentEmail: String, newEmail: String)
}
