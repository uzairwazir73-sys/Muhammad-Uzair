package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val passwordHash: String = "",
    val fullName: String,
    val fatherName: String = "",
    val cnic: String = "",
    val phone: String = "",
    val dob: String = "",
    val gender: String = "Male",
    val province: String = "Punjab",
    val district: String = "",
    val address: String = "",
    val educationLevel: String = "",
    val skills: String = "",
    val experience: String = "",
    val profilePicUri: String = "",
    val authProvider: String = "EMAIL", // "GOOGLE" or "EMAIL"
    val googleId: String = "",
    val role: String = "USER", // "USER", "SUPER_ADMIN", "CONTENT_MANAGER", "APPLICATION_MANAGER", "SUPPORT_STAFF", "EDITOR"
    val isBlocked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastActiveAt: Long = System.currentTimeMillis()
)
