package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ApplicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications ORDER BY submissionDate DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE userId = :userId ORDER BY submissionDate DESC")
    fun getApplicationsByUserId(userId: Long): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE id = :id")
    fun getApplicationById(id: Long): Flow<ApplicationEntity?>

    @Query("SELECT * FROM applications WHERE applicationNumber = :appNumber LIMIT 1")
    suspend fun getApplicationByNumber(appNumber: String): ApplicationEntity?

    @Query("SELECT * FROM applications WHERE status = :status ORDER BY submissionDate DESC")
    fun getApplicationsByStatus(status: String): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ApplicationEntity): Long

    @Update
    suspend fun updateApplication(application: ApplicationEntity)

    @Query("UPDATE applications SET status = :status, statusNote = :note, internalAdminNote = :internalNote, lastUpdatedDate = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, note: String, internalNote: String, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM applications")
    fun getTotalApplicationsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM applications WHERE status = 'SUBMITTED' OR status = 'UNDER_REVIEW'")
    fun getPendingApplicationsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM applications WHERE status = 'APPROVED'")
    fun getApprovedApplicationsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM applications WHERE userId = :userId")
    fun getUserApplicationsCount(userId: Long): Flow<Int>
}
