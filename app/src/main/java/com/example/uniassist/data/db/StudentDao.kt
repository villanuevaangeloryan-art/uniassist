package com.example.uniassist.data.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    // Flow allows the UI to automatically update if the database changes
    @Query("SELECT * FROM students WHERE isRegistered = 1")
    fun getAllRegisteredStudents(): Flow<List<StudentEntity>>
}