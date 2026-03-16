package com.example.uniassist.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow



@Dao
interface TaskDao {

    // Observe ALL tasks as a live stream
    @Query("SELECT * FROM tasks ORDER BY createdAt ASC")
    fun getAllFlow(): Flow<List<TaskEntity>>

    // Observe tasks for a specific day only
    @Query("SELECT * FROM tasks WHERE dateKey = :dateKey ORDER BY createdAt ASC")
    fun getByDateFlow(dateKey: String): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Int)
}