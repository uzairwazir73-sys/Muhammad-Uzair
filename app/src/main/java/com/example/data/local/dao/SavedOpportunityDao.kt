package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SavedOpportunityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedOpportunityDao {
    @Query("SELECT opportunityId FROM saved_opportunities WHERE userId = :userId")
    fun getSavedOpportunityIds(userId: Long): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOpportunity(saved: SavedOpportunityEntity)

    @Query("DELETE FROM saved_opportunities WHERE userId = :userId AND opportunityId = :opportunityId")
    suspend fun removeSavedOpportunity(userId: Long, opportunityId: Long)

    @Query("SELECT COUNT(*) > 0 FROM saved_opportunities WHERE userId = :userId AND opportunityId = :opportunityId")
    suspend fun isOpportunitySaved(userId: Long, opportunityId: Long): Boolean
}
