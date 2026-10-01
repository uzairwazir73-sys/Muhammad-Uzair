package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 0, // 0 means broadcast to all users
    val title: String,
    val message: String,
    val category: String = "GENERAL", // "JOB", "ADMISSION", "SCHOLARSHIP", "APPLICATION_UPDATE", "GENERAL"
    val link: String = "",
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
