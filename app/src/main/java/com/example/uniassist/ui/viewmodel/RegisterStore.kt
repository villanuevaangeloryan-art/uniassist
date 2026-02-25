package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniassist.domain.actions.RegisterAction
import com.example.uniassist.domain.reducers.RegisterReducer
import com.example.uniassist.domain.state.RegisterState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterStore : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    fun dispatch(action: RegisterAction) {
        viewModelScope.launch {
            _state.update { currentState ->
                RegisterReducer.reduce(currentState, action)
            }

            // Handle side effects
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
        _state.update { it.copy(isLoading = true) }

        // Simulate network delay
        delay(2000)

        // Determine user type based on email domain
        val userType = determineUserType(_state.value.email)

        _state.update {
            it.copy(
                isLoading = false,
                navigateToDashboard = true,
                userType = userType
            )
        }
    }

    private fun determineUserType(email: String): String {
        return when {
            email.endsWith("@pnc.edu.ph", ignoreCase = true) -> "admin"
            email.endsWith("@gmail.com", ignoreCase = true) -> "student"
            else -> "student" // Default to student
        }
    }

    fun resetNavigation() {
        dispatch(RegisterAction.ResetNavigation)
    }
}