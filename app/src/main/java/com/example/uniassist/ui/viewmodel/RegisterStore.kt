package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.FirebaseAuthRepository
import com.example.uniassist.domain.actions.RegisterAction
import com.example.uniassist.domain.reducers.RegisterReducer
import com.example.uniassist.domain.state.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterStore(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    fun dispatch(action: RegisterAction) {
        viewModelScope.launch {
            _state.update { currentState ->
                RegisterReducer.reduce(currentState, action)
            }
            when (action) {
                is RegisterAction.RegisterButtonClicked -> {
                    if (_state.value.isFormValid) {
                        performRegistration()
                    }
                }
                else -> {}
            }
        }
    }

    private suspend fun performRegistration() {
        _state.update { it.copy(isLoading = true, authError = "") }
        try {
            val state = _state.value

            // Step 1: Create the Firebase Auth account
            val uid = authRepository.signUp(state.email, state.password)

            // Step 2: Save the profile to Firestore
            authRepository.saveProfile(
                uid = uid,
                profileData = mapOf(
                    "firstName" to state.firstName,
                    "lastName" to state.lastName,
                    "email" to state.email,
                    "role" to "Student"
                )
            )

            // Step 3: Navigate on success
            _state.update {
                it.copy(
                    isLoading = false,
                    navigateToDashboard = true
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    authError = e.message ?: "Registration failed. Please try again."
                )
            }
        }
    }

    fun resetNavigation() = dispatch(RegisterAction.ResetNavigation)
}