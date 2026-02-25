package com.example.uniassist.domain.reducer

import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.domain.state.LoginState
import com.example.uniassist.domain.utils.ValidationRules

class LoginReducer {
    fun reduce(state: LoginState, action: LoginAction): LoginState {
        return when (action) {
            is LoginAction.EmailChanged -> {
                val validation = ValidationRules.validateEmail(action.email)
                state.copy(
                    email = action.email,
                    emailError = validation.errorMessage
                ).validateForm()
            }
            is LoginAction.PasswordChanged -> {
                val validation = ValidationRules.validatePassword(action.password)
                state.copy(
                    password = action.password,
                    passwordError = validation.errorMessage
                ).validateForm()
            }
            is LoginAction.TogglePasswordVisibility -> {
                state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            is LoginAction.LoginButtonClicked -> {
                val emailValid = ValidationRules.validateEmail(state.email)
                val passwordValid = ValidationRules.validatePassword(state.password)
                state.copy(
                    emailError = emailValid.errorMessage,
                    passwordError = passwordValid.errorMessage,
                    isLoading = emailValid.isValid && passwordValid.isValid
                ).validateForm()
            }
            is LoginAction.NavigateToDashboard -> {
                state.copy(navigateToDashboard = true)
            }
            is LoginAction.NavigateToRegister -> {
                state.copy(navigateToRegister = true)
            }
            is LoginAction.NavigateBack -> {
                state.copy(navigateBack = true)
            }
            is LoginAction.NavigationHandled -> {
                state.copy(
                    navigateToDashboard = false,
                    navigateToRegister = false,
                    navigateBack = false
                )
            }
            is LoginAction.ResetNavigation -> {
                state.copy(
                    navigateToDashboard = false,
                    navigateToRegister = false,
                    navigateBack = false
                )
            }
            is LoginAction.LoadingComplete -> {
                // Keep isLoading true to avoid showing login UI during navigation
                state.copy(
                    navigateToDashboard = true
                )
            }
        }
    }

    private fun LoginState.validateForm(): LoginState {
        val isValid = !hasEmailError && !hasPasswordError && email.isNotBlank() && password.isNotBlank()
        return copy(isFormValid = isValid)
    }
}