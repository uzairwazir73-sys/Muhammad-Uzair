package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val adminName: String,
    val adminEmail: String,
    val action: String,
    val targetRecord: String,
    val timestamp: Long = System.currentTimeMillis()
)
