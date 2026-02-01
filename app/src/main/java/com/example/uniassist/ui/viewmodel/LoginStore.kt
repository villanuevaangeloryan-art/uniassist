package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.domain.reducer.LoginReducer
import com.example.uniassist.domain.state.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginStore : ViewModel() {

    private val reducer = LoginReducer()

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun dispatch(action: LoginAction) {
        // Get current state
        val currentState = _state.value

        // Reduce to get new state
        val newState = reducer.reduce(currentState, action)

        // Update state
        _state.value = newState

        // Handle side effects
        handleSideEffects(action)
    }

    private fun handleSideEffects(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.LoginButtonClicked -> {
                    // Only proceed if form is valid
                    if (_state.value.isFormValid) {
                        // Simulate API call/login process (2 seconds)
                        delay(2000)

                        // Dispatch navigation action on successful login
                        dispatch(LoginAction.NavigateToHome)
                    }
                }
                else -> {
                    // No side effects for other actions
                }
            }
        }
    }

    // Helper to reset navigation flags
    fun resetNavigation() {
        dispatch(LoginAction.NavigationHandled)
    }
}