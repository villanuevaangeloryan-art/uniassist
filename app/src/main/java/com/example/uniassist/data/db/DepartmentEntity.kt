package com.example.uniassist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "departments",
    foreignKeys = [
        ForeignKey(
            entity = CampusEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("campusId"),
            onDelete = ForeignKey.CASCADE // If a campus is deleted, its departments are too
        )
    ]
)
data class DepartmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val campusId: Int,
    val departmentName: String,
    val description: String
)