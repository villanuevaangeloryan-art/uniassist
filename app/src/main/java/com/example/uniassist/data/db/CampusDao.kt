package com.example.uniassist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CampusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampus(campus: CampusEntity)

    @Update
    suspend fun updateCampus(campus: CampusEntity)

    @Query("SELECT * FROM campuses LIMIT 1")
    fun getCampusInfo(): Flow<CampusEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: DepartmentEntity)

    @Query("SELECT * FROM departments")
    fun getAllDepartments(): Flow<List<DepartmentEntity>>
}