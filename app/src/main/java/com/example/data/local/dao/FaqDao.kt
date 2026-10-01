package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.FaqEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FaqDao {
    @Query("SELECT * FROM faqs ORDER BY orderIndex ASC")
    fun getAllFaqs(): Flow<List<FaqEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaq(faq: FaqEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaqs(faqs: List<FaqEntity>)

    @Update
    suspend fun updateFaq(faq: FaqEntity)

    @Delete
    suspend fun deleteFaq(faq: FaqEntity)
}
