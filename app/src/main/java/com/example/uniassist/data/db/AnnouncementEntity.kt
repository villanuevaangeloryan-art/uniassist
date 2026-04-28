package com.example.uniassist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val date: String,
    val content: String,
    val bulletsRaw: String,
    val footer: String,
    val category: String,
    val isPriority: Boolean,
    val isRead: Boolean = false,
    val isArchived: Boolean = false,
    val isDraft: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)