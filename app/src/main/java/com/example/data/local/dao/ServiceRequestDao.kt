package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ServiceRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceRequestDao {
    @Query("SELECT * FROM service_requests ORDER BY requestedAt DESC")
    fun getAllRequests(): Flow<List<ServiceRequestEntity>>

    @Query("SELECT * FROM service_requests WHERE userId = :userId ORDER BY requestedAt DESC")
    fun getRequestsByUserId(userId: Long): Flow<List<ServiceRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: ServiceRequestEntity): Long

    @Update
    suspend fun updateRequest(request: ServiceRequestEntity)

    @Query("UPDATE service_requests SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
}
