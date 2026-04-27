package com.example.uniassist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String, // e.g., "2023-0001"
    val fullName: String,
    val course: String,
    val isRegistered: Boolean = true
)