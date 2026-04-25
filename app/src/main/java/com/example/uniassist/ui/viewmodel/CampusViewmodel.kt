package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.db.CampusEntity
import com.example.uniassist.data.db.CampusRepository
import com.example.uniassist.data.db.DepartmentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CampusViewModel(private val repository: CampusRepository) : ViewModel() {

    // State for the Campus Info
    private val _campusInfo = MutableStateFlow<CampusEntity?>(null)
    val campusInfo: StateFlow<CampusEntity?> = _campusInfo.asStateFlow()

    // State for the list of Departments
    private val _departments = MutableStateFlow<List<DepartmentEntity>>(emptyList())
    val departments: StateFlow<List<DepartmentEntity>> = _departments.asStateFlow()

    init {
        // Load data immediately when the ViewModel is created
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getCampusDetails().collect { campus ->
                _campusInfo.value = campus
            }
        }
        viewModelScope.launch {
            repository.getAllDepartments().collect { deptList ->
                _departments.value = deptList
            }
        }
    }

    fun saveCampusInfo(name: String, address: String, contactNumber: String) {
        viewModelScope.launch {
            // Hardcoding ID to 1 ensures we only ever have ONE campus record that gets updated
            val campus = CampusEntity(id = 1, name = name, address = address, contactNumber = contactNumber)
            repository.addOrUpdateCampus(campus)
        }
    }

    fun addDepartment(campusId: Int, name: String, description: String) {
        viewModelScope.launch {
            val department = DepartmentEntity(campusId = campusId, departmentName = name, description = description)
            repository.addDepartment(department)
        }
    }
}

// We need a Factory because our ViewModel takes a Repository as a parameter
class CampusViewModelFactory(private val repository: CampusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CampusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CampusViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}