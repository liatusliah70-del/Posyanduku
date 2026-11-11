package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY fullName ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = :role ORDER BY fullName ASC")
    fun getUsersByRole(role: UserRole): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE email = :email OR username = :email LIMIT 1")
    suspend fun getUserByEmailOrUsername(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: String)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM users WHERE role = 'PETUGAS'")
    fun getPetugasCount(): Flow<Int>
}
