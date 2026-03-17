package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.TaskRepository
import com.example.uniassist.ui.screens.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ── ViewModel

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    // The currently selected calendar date. When the student taps a day,
    // the screen calls setSelectedDate() and tasksForSelectedDate updates.
    private val _selectedDateKey = MutableStateFlow(todayKey())
    val selectedDateKey: StateFlow<String> = _selectedDateKey

    // Tasks for the selected date — reacts automatically when either the
    // selected date changes OR the database changes.
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val tasksForSelectedDate: StateFlow<List<TaskItem>> = _selectedDateKey
        .flatMapLatest { dateKey -> repository.tasksForDate(dateKey) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun setSelectedDate(dateKey: String) {
        _selectedDateKey.value = dateKey
    }

    fun addTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addTask(title, _selectedDateKey.value)
        }
    }

    fun updateTask(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun toggleComplete(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTask(task.copy(completed = !task.completed))
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    // ── Factory
    class Factory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            TaskViewModel(repository) as T
    }

    companion object {
        fun todayKey(): String {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            return sdf.format(java.util.Date())
        }
    }
}