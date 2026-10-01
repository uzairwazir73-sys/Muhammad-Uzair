package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_requests")
data class ServiceRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val requestId: String,
    val userId: Long,
    val applicantName: String,
    val serviceType: String,
    val details: String,
    val contactPhone: String,
    val status: String = "PENDING", // "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    val requestedAt: Long = System.currentTimeMillis()
)
