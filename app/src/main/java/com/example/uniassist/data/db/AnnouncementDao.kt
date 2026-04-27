package com.example.uniassist.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow


// Room DAO
@Dao
interface AnnouncementDao {

    // Observe all announcements, newest first. Used by teacher (includes archived).
    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<AnnouncementEntity>>

    // Observe only non-archived, non-draft announcements. Used by student dashboard.
    @Query("SELECT * FROM announcements WHERE isArchived = 0 AND isDraft = 0 ORDER BY createdAt DESC")
    fun getActiveFlow(): Flow<List<AnnouncementEntity>>

    // ── NEW: Observe only drafts ──────────────────────────────────────────────
    @Query("SELECT * FROM announcements WHERE isDraft = 1 ORDER BY createdAt DESC")
    fun getDraftsFlow(): Flow<List<AnnouncementEntity>>

    // ── NEW: Observe only archived (non-draft) ────────────────────────────────
    @Query("SELECT * FROM announcements WHERE isArchived = 1 AND isDraft = 0 ORDER BY createdAt DESC")
    fun getArchivedFlow(): Flow<List<AnnouncementEntity>>

    // Archive a single announcement.
    @Query("UPDATE announcements SET isArchived = 1 WHERE id = :id")
    suspend fun archive(id: Int)

    // ── NEW: Restore an archived announcement back to active ──────────────────
    @Query("UPDATE announcements SET isArchived = 0 WHERE id = :id")
    suspend fun unarchive(id: Int)

    // ── NEW: Publish a draft (clear isDraft flag) ─────────────────────────────
    @Query("UPDATE announcements SET isDraft = 0 WHERE id = :id")
    suspend fun publishDraft(id: Int)

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
    @Query("SELECT COUNT(*) FROM announcements WHERE isRead = 0 AND isDraft = 0 AND isArchived = 0")
    fun getUnreadCountFlow(): Flow<Int>

    // Delete one announcement.
    @Delete
    suspend fun delete(announcement: AnnouncementEntity)

    // Delete all
    @Query("DELETE FROM announcements")
    suspend fun deleteAll()
}
