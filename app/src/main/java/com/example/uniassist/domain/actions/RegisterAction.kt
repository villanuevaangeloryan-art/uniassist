package com.example.uniassist.domain.actions

sealed class RegisterAction {
    data class FirstNameChanged(val firstName: String) : RegisterAction()
    data class LastNameChanged(val lastName: String) : RegisterAction()
    data class EmailChanged(val email: String) : RegisterAction()
    data class UsernameChanged(val username: String) : RegisterAction()
    data class PasswordChanged(val password: String) : RegisterAction()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterAction()

    object TogglePasswordVisibility : RegisterAction()
    object RegisterButtonClicked : RegisterAction()
    object NavigateToDashboard : RegisterAction()
    object NavigateBack : RegisterAction()
    object ResetNavigation : RegisterAction()
}