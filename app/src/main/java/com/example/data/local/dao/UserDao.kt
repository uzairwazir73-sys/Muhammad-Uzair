package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserByIdOnce(userId: Long): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isBlocked = :isBlocked WHERE id = :userId")
    suspend fun setUserBlocked(userId: Long, isBlocked: Boolean)

    @Query("UPDATE users SET role = :newRole WHERE id = :userId")
    suspend fun updateUserRole(userId: Long, newRole: String)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}
