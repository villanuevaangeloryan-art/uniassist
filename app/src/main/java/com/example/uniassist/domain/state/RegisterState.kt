package com.example.uniassist.domain.state

data class RegisterState(
    // Form Fields
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,

    // Validation Errors
    val firstNameError: String = "",
    val lastNameError: String = "",
    val emailError: String = "",
    val usernameError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",

    // Auth/Server Error
    val authError: String = "",

    // UI State
    val isLoading: Boolean = false,

    // Navigation Flags
    val navigateToDashboard: Boolean = false,
    val navigateBack: Boolean = false,

    // Form Validity
    val isFormValid: Boolean = false,

    // User Type (student or admin)
    val userType: String = "student"
) {
    val hasFirstNameError: Boolean get() = firstNameError.isNotEmpty()
    val hasLastNameError: Boolean get() = lastNameError.isNotEmpty()
    val hasEmailError: Boolean get() = emailError.isNotEmpty()
    val hasUsernameError: Boolean get() = usernameError.isNotEmpty()
    val hasPasswordError: Boolean get() = passwordError.isNotEmpty()
    val hasConfirmPasswordError: Boolean get() = confirmPasswordError.isNotEmpty()
    val hasAuthError: Boolean get() = authError.isNotEmpty()
}