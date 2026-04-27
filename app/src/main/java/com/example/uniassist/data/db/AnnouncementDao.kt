package com.example.uniassist.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow


// Room DAO
@Dao
interface AnnouncementDao {

    // Observe all announcements, newest first. Used by both teacher and student.
    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<AnnouncementEntity>>

    // Insert a newly published announcement.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(announcement: AnnouncementEntity)

    // Mark a single announcement as read (student side).
    @Query("UPDATE announcements SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    // Mark ALL announcements as read.
    @Query("UPDATE announcements SET isRead = 1")   
    suspend fun markAllAsRead()

    // Count unread announcements
    @Query("SELECT COUNT(*) FROM announcements WHERE isRead = 0")
    fun getUnreadCountFlow(): Flow<Int>

    // Delete one announcement.
    @Delete
    suspend fun delete(announcement: AnnouncementEntity)

    // Delete all
    @Query("DELETE FROM announcements")
    suspend fun deleteAll()
}