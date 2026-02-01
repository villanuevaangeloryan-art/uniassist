package com.example.uniassist.domain.utils

object ValidationRules {
    fun validateEmail(email: String): String {
        return when {
            email.isEmpty() -> "Email is required"
            // Simple email validation regex
            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")) ->
                "Please enter a valid email address"
            else -> ""
        }
    }

    fun validatePassword(password: String): String {
        return when {
            password.isEmpty() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> ""
        }
    }
}