package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.OpportunityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OpportunityDao {
    @Query("SELECT * FROM opportunities WHERE status = 'PUBLISHED' ORDER BY createdAt DESC")
    fun getPublishedOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities ORDER BY createdAt DESC")
    fun getAllOpportunities(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE id = :id")
    fun getOpportunityById(id: Long): Flow<OpportunityEntity?>

    @Query("SELECT * FROM opportunities WHERE id = :id")
    suspend fun getOpportunityByIdOnce(id: Long): OpportunityEntity?

    @Query("SELECT * FROM opportunities WHERE category = :category AND status = 'PUBLISHED' ORDER BY createdAt DESC")
    fun getOpportunitiesByCategory(category: String): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM opportunities WHERE isFeatured = 1 AND status = 'PUBLISHED' ORDER BY createdAt DESC LIMIT 5")
    fun getFeaturedOpportunities(): Flow<List<OpportunityEntity>>

    @Query("""
        SELECT * FROM opportunities 
        WHERE status = 'PUBLISHED' 
        AND (title LIKE '%' || :query || '%' 
             OR organization LIKE '%' || :query || '%' 
             OR qualificationRequired LIKE '%' || :query || '%' 
             OR city LIKE '%' || :query || '%'
             OR province LIKE '%' || :query || '%')
        ORDER BY createdAt DESC
    """)
    fun searchOpportunities(query: String): Flow<List<OpportunityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunity(opportunity: OpportunityEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunities(opportunities: List<OpportunityEntity>)

    @Update
    suspend fun updateOpportunity(opportunity: OpportunityEntity)

    @Delete
    suspend fun deleteOpportunity(opportunity: OpportunityEntity)

    @Query("DELETE FROM opportunities WHERE id = :id")
    suspend fun deleteOpportunityById(id: Long)

    @Query("UPDATE opportunities SET status = :status WHERE id = :id")
    suspend fun updateOpportunityStatus(id: Long, status: String)

    @Query("UPDATE opportunities SET totalApplicationsCount = totalApplicationsCount + 1 WHERE id = :id")
    suspend fun incrementApplicationCount(id: Long)

    @Query("SELECT COUNT(*) FROM opportunities")
    fun getTotalOpportunityCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM opportunities WHERE status = 'PUBLISHED'")
    fun getActiveOpportunityCount(): Flow<Int>
}
