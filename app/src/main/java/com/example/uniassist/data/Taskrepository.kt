package com.example.uniassist.data

import com.example.uniassist.data.db.TaskDao
import com.example.uniassist.data.db.TaskEntity
import com.example.uniassist.ui.screens.TaskItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ── Repository


class TaskRepository(private val dao: TaskDao) {

    // All tasks as a live stream of UI models.
    val allTasks: Flow<List<TaskItem>> = dao.getAllFlow().map { entities ->
        entities.map { it.toUiModel() }
    }

    // Tasks for a specific date as a live stream.
    fun tasksForDate(dateKey: String): Flow<List<TaskItem>> =
        dao.getByDateFlow(dateKey).map { entities -> entities.map { it.toUiModel() } }

    suspend fun addTask(title: String, dateKey: String) {
        dao.insert(TaskEntity(title = title, dateKey = dateKey))
    }

    suspend fun updateTask(task: TaskItem) {
        dao.update(task.toEntity())
    }

    suspend fun deleteTask(id: Int) {
        dao.deleteById(id)
    }
}


fun TaskEntity.toUiModel() = TaskItem(
    id        = id,
    title     = title,
    dateKey   = dateKey,
    completed = completed
)

fun TaskItem.toEntity() = TaskEntity(
    id        = id,
    title     = title,
    dateKey   = dateKey,
    completed = completed
)