package com.example.uniassist.domain.reducers

import com.example.uniassist.domain.actions.RegisterAction
import com.example.uniassist.domain.state.RegisterState
import com.example.uniassist.domain.utils.ValidationRules

object RegisterReducer {
    fun reduce(state: RegisterState, action: RegisterAction): RegisterState {
        return when (action) {
            is RegisterAction.FirstNameChanged -> {
                val validation = ValidationRules.validateName(action.firstName)
                state.copy(
                    firstName = action.firstName,
                    firstNameError = validation.errorMessage
                ).validateForm()
            }
            is RegisterAction.LastNameChanged -> {
                val validation = ValidationRules.validateName(action.lastName)
                state.copy(
                    lastName = action.lastName,
                    lastNameError = validation.errorMessage
                ).validateForm()
            }
            is RegisterAction.EmailChanged -> {
                val validation = ValidationRules.validateEmail(action.email)
                state.copy(
                    email = action.email,
                    emailError = validation.errorMessage
                ).validateForm()
            }
            is RegisterAction.UsernameChanged -> {
                val validation = ValidationRules.validateUsername(action.username)
                state.copy(
                    username = action.username,
                    usernameError = validation.errorMessage
                ).validateForm()
            }
            is RegisterAction.PasswordChanged -> {
                val validation = ValidationRules.validatePassword(action.password)
                val confirmValidation = if (state.confirmPassword.isNotBlank()) {
                    ValidationRules.validateConfirmPassword(action.password, state.confirmPassword)
                } else {
                    ValidationRules.ValidationResult(true, "")
                }
                state.copy(
                    password = action.password,
                    passwordError = validation.errorMessage,
                    confirmPasswordError = if (state.confirmPassword.isNotBlank())
                        confirmValidation.errorMessage
                    else
                        state.confirmPasswordError
                ).validateForm()
            }
            is RegisterAction.ConfirmPasswordChanged -> {
                val validation = ValidationRules.validateConfirmPassword(state.password, action.confirmPassword)
                state.copy(
                    confirmPassword = action.confirmPassword,
                    confirmPasswordError = validation.errorMessage
                ).validateForm()
            }
            is RegisterAction.TogglePasswordVisibility -> {
                state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            is RegisterAction.RegisterButtonClicked -> {
                val firstNameValidation = ValidationRules.validateName(state.firstName)
                val lastNameValidation = ValidationRules.validateName(state.lastName)
                val emailValidation = ValidationRules.validateEmail(state.email)
                val usernameValidation = ValidationRules.validateUsername(state.username)
                val passwordValidation = ValidationRules.validatePassword(state.password)
                val confirmValidation = ValidationRules.validateConfirmPassword(state.password, state.confirmPassword)

                state.copy(
                    firstNameError = firstNameValidation.errorMessage,
                    lastNameError = lastNameValidation.errorMessage,
                    emailError = emailValidation.errorMessage,
                    usernameError = usernameValidation.errorMessage,
                    passwordError = passwordValidation.errorMessage,
                    confirmPasswordError = confirmValidation.errorMessage,
                    isLoading = firstNameValidation.isValid && lastNameValidation.isValid &&
                            emailValidation.isValid && usernameValidation.isValid &&
                            passwordValidation.isValid && confirmValidation.isValid
                ).validateForm()
            }
            is RegisterAction.NavigateToDashboard -> {
                state.copy(navigateToDashboard = true)
            }
            is RegisterAction.NavigateBack -> {
                state.copy(navigateBack = true)
            }
            is RegisterAction.ResetNavigation -> {
                state.copy(
                    navigateToDashboard = false,
                    navigateBack = false
                )
            }
            is RegisterAction.LoadingComplete -> {
                // Keep isLoading true to avoid showing register UI during navigation
                state.copy(
                    navigateToDashboard = true
                )
            }
        }
    }

    private fun RegisterState.validateForm(): RegisterState {
        val isFormValid = !hasFirstNameError && !hasLastNameError && !hasEmailError &&
                !hasUsernameError && !hasPasswordError && !hasConfirmPasswordError &&
                firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() &&
                username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
        return this.copy(isFormValid = isFormValid)
    }
}