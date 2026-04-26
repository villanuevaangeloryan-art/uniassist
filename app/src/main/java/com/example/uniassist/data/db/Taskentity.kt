package com.example.uniassist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val dateKey: String,      // "yyyy-MM-dd"
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)