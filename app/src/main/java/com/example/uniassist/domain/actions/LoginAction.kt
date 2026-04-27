package com.example.uniassist.domain.actions

sealed class LoginAction {
    data class EmailChanged(val email: String) : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    data object TogglePasswordVisibility : LoginAction()

    data class LoginButtonClicked(val expectedRole: String) : LoginAction()
    data class GoogleSignInClicked(val idToken: String) : LoginAction() // NEW

    data object NavigateToDashboard : LoginAction()
    data object NavigateToRegister : LoginAction()
    data object NavigateBack : LoginAction()
    data object NavigationHandled : LoginAction()
    data object ResetNavigation : LoginAction()
    data object LoadingComplete : LoginAction()
}