package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId OR userId = 0 ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: Long): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE (userId = :userId OR userId = 0) AND isRead = 0")
    fun getUnreadCount(userId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId OR userId = 0")
    suspend fun markAllAsRead(userId: Long)

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Long)
}
