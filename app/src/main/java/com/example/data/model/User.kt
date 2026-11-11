package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    ADMIN,
    PETUGAS
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val username: String,
    val fullName: String,
    val role: UserRole,
    val assignedKecamatan: String? = null,
    val assignedDesa: String? = null,
    val assignedPosyanduId: Long? = null,
    val phone: String = "",
    val nip: String = "",
    val jabatan: String = "",
    val passwordHash: String = "posyandu2026"
)
