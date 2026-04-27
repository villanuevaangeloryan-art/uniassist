package com.example.uniassist.data.db


import kotlinx.coroutines.flow.Flow

class StudentRepository(private val studentDao: StudentDao) {

    fun getRegisteredStudents(): Flow<List<StudentEntity>> {
        return studentDao.getAllRegisteredStudents()
    }

    // Optional: Add a function to insert a student so you can test it!
    suspend fun addTestStudent(student: StudentEntity) {
        // You would need to add an insert function in StudentDao for this to work
    }
}