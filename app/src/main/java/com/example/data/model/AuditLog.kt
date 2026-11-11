package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val waktuFormatted: String,
    val userEmail: String,
    val userName: String,
    val userRole: String,
    val action: String,
    val entityType: String,
    val details: String
)
