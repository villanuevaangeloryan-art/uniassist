package com.example.uniassist.domain.reducer

import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.domain.state.LoginState
import com.example.uniassist.domain.utils.ValidationRules

class LoginReducer {

    fun reduce(state: LoginState, action: LoginAction): LoginState {
        return when (action) {
            // Form Input Actions
            is LoginAction.EmailChanged -> {
                val errorMessage = ValidationRules.validateEmail(action.email)
                state.copy(
                    email = action.email,
                    emailError = errorMessage,
                    isFormValid = errorMessage.isEmpty() &&
                            ValidationRules.validatePassword(state.password).isEmpty()
                )
            }

            is LoginAction.PasswordChanged -> {
                val errorMessage = ValidationRules.validatePassword(action.password)
                state.copy(
                    password = action.password,
                    passwordError = errorMessage,
                    isFormValid = ValidationRules.validateEmail(state.email).isEmpty() &&
                            errorMessage.isEmpty()
                )
            }

            is LoginAction.TogglePasswordVisibility -> {
                state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            // Form Submission
            is LoginAction.LoginButtonClicked -> {
                val emailError = ValidationRules.validateEmail(state.email)
                val passwordError = ValidationRules.validatePassword(state.password)
                val isFormValid = emailError.isEmpty() && passwordError.isEmpty()

                state.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    isLoading = isFormValid,
                    isFormValid = isFormValid
                )
            }

            // Navigation Actions
            is LoginAction.NavigateToHome -> {
                state.copy(
                    isLoading = false,
                    navigateToHome = true
                )
            }

            is LoginAction.NavigateToRegister -> {
                state.copy(
                    navigateToRegister = true
                )
            }

            is LoginAction.NavigateBack -> {
                state.copy(
                    navigateBack = true
                )
            }

            is LoginAction.NavigationHandled -> {
                state.copy(
                    navigateToHome = false,
                    navigateToRegister = false,
                    navigateBack = false
                )
            }
        }
    }
}