package com.example.uniassist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campuses")
data class CampusEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val contactNumber: String
)