package com.example.uniassist.data.db

import kotlinx.coroutines.flow.Flow

class CampusRepository(private val campusDao: CampusDao) {

    suspend fun addOrUpdateCampus(campus: CampusEntity) {
        campusDao.insertCampus(campus)
    }

    fun getCampusDetails(): Flow<CampusEntity?> {
        return campusDao.getCampusInfo()
    }

    suspend fun addDepartment(department: DepartmentEntity) {
        campusDao.insertDepartment(department)
    }

    fun getAllDepartments(): Flow<List<DepartmentEntity>> {
        return campusDao.getAllDepartments()
    }
}