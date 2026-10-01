package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_opportunities")
data class SavedOpportunityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val opportunityId: Long,
    val savedAt: Long = System.currentTimeMillis()
)
