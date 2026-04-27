package com.example.uniassist.data

// The default values ("") are REQUIRED for Firebase to deserialize the data without crashing.
data class StudentModel(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val course: String = "Not Specified",
    val role: String = "Student"
) {
    // Helper property to combine names for the UI
    val fullName: String get() = "$firstName $lastName"
}