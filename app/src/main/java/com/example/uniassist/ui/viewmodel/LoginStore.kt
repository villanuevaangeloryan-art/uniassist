package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.FirebaseAuthRepository
import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.domain.reducer.LoginReducer
import com.example.uniassist.domain.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginStore(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    // Using StateFlow to handle the MVI state updates
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state
    private val reducer = LoginReducer()

    // This handles all the clicks and typing from the Login Screen
    fun dispatch(action: LoginAction) {
        viewModelScope.launch {
            // First, update the state based on the action (like typing email)
            _state.update { current ->
                reducer.reduce(current, action)
            }

            // If the button was clicked and the form is valid, start the login process
            when (action) {
                is LoginAction.LoginButtonClicked -> {
                    if (_state.value.isFormValid) performLogin()
                }
                else -> {}
            }
        }
    }

    private suspend fun performLogin() {
        // Reset errors and show the loading spinner
        _state.update { it.copy(isLoading = true, authError = "") }

        try {
            // Call the repository to talk to Firebase
            val authResult = authRepository.signIn(
                email = _state.value.email,
                password = _state.value.password
            )

            // If we reach here, login worked! Save the data and trigger navigation
            _state.update {
                it.copy(
                    isLoading = false,
                    userRole = authResult.role, // This role determines which dashboard they see
                    firstName = authResult.firstName,
                    lastName = authResult.lastName,
                    navigateToDashboard = true
                )
            }
        } catch (e: Exception) {
            // If login fails, show the error message and stop the loader
            _state.update {
                it.copy(
                    isLoading = false,
                    authError = e.message ?: "Login failed. Check your connection."
                )
            }
        }
    }

    // Helper to stop the app from navigating in a loop
    fun resetNavigation() = dispatch(LoginAction.ResetNavigation)
}