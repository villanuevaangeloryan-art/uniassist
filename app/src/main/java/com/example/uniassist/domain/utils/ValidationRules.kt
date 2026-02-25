package com.example.uniassist.domain.utils

import android.util.Patterns

object ValidationRules {

    data class ValidationResult(val isValid: Boolean, val errorMessage: String)

    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "This field is required")
            name.length < 2 -> ValidationResult(false, "Must be at least 2 characters")
            !name.matches(Regex("^[a-zA-Z\\s-]+$")) -> ValidationResult(false, "Only letters, spaces, and hyphens allowed")
            else -> ValidationResult(true, "")
        }
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "Email is required")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult(false, "Please enter a valid email address")
            else -> ValidationResult(true, "")
        }
    }

    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult(false, "Username is required")
            username.length < 3 -> ValidationResult(false, "Must be at least 3 characters")
            username.length > 20 -> ValidationResult(false, "Must be less than 20 characters")
            !username.matches(Regex("^[a-zA-Z0-9._-]+$")) ->
                ValidationResult(false, "Only letters, numbers, and . _ - allowed")
            else -> ValidationResult(true, "")
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "Password is required")
            password.length < 8 -> ValidationResult(false, "Must be at least 8 characters")
            !password.any { it.isUpperCase() } ->
                ValidationResult(false, "Must contain at least one uppercase letter")
            !password.any { it.isLowerCase() } ->
                ValidationResult(false, "Must contain at least one lowercase letter")
            !password.any { it.isDigit() } ->
                ValidationResult(false, "Must contain at least one number")
            !password.any { !it.isLetterOrDigit() } ->
                ValidationResult(false, "Must contain at least one special character")
            else -> ValidationResult(true, "")
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult(false, "Please confirm your password")
            password != confirmPassword -> ValidationResult(false, "Passwords do not match")
            else -> ValidationResult(true, "")
        }
    }
}