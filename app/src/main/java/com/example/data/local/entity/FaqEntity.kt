package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faqs")
data class FaqEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val question: String,
    val answer: String,
    val category: String = "General",
    val orderIndex: Int = 0
)
