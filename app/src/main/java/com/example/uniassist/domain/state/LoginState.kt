package com.example.uniassist.domain.state

data class LoginState(
    // Form Fields
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,

    // Validation Errors
    val emailError: String = "",
    val passwordError: String = "",

    // UI State
    val isLoading: Boolean = false,

    // Navigation Flags
    val navigateToHome: Boolean = false,
    val navigateToRegister: Boolean = false,
    val navigateBack: Boolean = false,

    // Form Validity
    val isFormValid: Boolean = false
) {
    // Helper computed properties
    val hasEmailError: Boolean get() = emailError.isNotEmpty()
    val hasPasswordError: Boolean get() = passwordError.isNotEmpty()
}