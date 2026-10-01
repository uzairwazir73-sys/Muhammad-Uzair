package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String = "GENERAL", // "GOV_JOB", "ADMISSION", "SCHOLARSHIP", "HAJJ", "UMRAH", "GENERAL"
    val actionUrl: String = "",
    val badgeText: String = "Important",
    val isUrgent: Boolean = false,
    val publishedAt: Long = System.currentTimeMillis()
)
