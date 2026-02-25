package com.example.uniassist.domain.actions

sealed class LoginAction {
    // Form Input Actions
    data class EmailChanged(val email: String) : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    data object TogglePasswordVisibility : LoginAction()

    // Form Submission
    data object LoginButtonClicked : LoginAction()

    // Navigation Actions
    data object NavigateToDashboard : LoginAction()   // Changed from NavigateToHome
    data object NavigateToRegister : LoginAction()
    data object NavigateBack : LoginAction()
    data object NavigationHandled : LoginAction()
    data object ResetNavigation : LoginAction()
}