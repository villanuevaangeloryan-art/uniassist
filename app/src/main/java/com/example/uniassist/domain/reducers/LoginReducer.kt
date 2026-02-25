package com.example.uniassist.domain.reducers

import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.domain.state.LoginState
import com.example.uniassist.domain.utils.ValidationRules

class LoginReducer {

    fun reduce(state: LoginState, action: LoginAction): LoginState {
        return when (action) {
            // Form Input Actions
            is LoginAction.EmailChanged -> {
                val validationResult = ValidationRules.validateEmail(action.email)
                state.copy(
                    email = action.email,
                    emailError = validationResult.errorMessage
                ).validateForm()
            }

            is LoginAction.PasswordChanged -> {
                val validationResult = ValidationRules.validatePassword(action.password)
                state.copy(
                    password = action.password,
                    passwordError = validationResult.errorMessage
                ).validateForm()
            }

            is LoginAction.TogglePasswordVisibility -> {
                state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            // Form Submission
            is LoginAction.LoginButtonClicked -> {
                val emailValidation = ValidationRules.validateEmail(state.email)
                val passwordValidation = ValidationRules.validatePassword(state.password)

                state.copy(
                    emailError = emailValidation.errorMessage,
                    passwordError = passwordValidation.errorMessage,
                    isLoading = emailValidation.isValid && passwordValidation.isValid
                ).validateForm()
            }

            // Navigation Actions
            is LoginAction.NavigateToDashboard -> {
                state.copy(
                    isLoading = false,
                    navigateToDashboard = true
                )
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
        }
    }

    private fun LoginState.validateForm(): LoginState {
        val isFormValid = !hasEmailError && !hasPasswordError &&
                email.isNotBlank() && password.isNotBlank()
        return this.copy(isFormValid = isFormValid)
    }
}