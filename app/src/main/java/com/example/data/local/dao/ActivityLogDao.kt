package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ActivityLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLogEntity): Long
}
