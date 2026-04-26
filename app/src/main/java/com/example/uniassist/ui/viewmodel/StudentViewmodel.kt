package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.FirestoreStudentRepository
import com.example.uniassist.data.StudentModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: FirestoreStudentRepository) : ViewModel() {

    private val _students = MutableStateFlow<List<StudentModel>>(emptyList())
    val students: StateFlow<List<StudentModel>> = _students.asStateFlow()

    init {
        viewModelScope.launch {
            // This now listens to the live Firebase stream
            repository.getRegisteredStudents().collect { studentList ->
                _students.value = studentList
            }
        }
    }
}

class StudentViewModelFactory(private val repository: FirestoreStudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}