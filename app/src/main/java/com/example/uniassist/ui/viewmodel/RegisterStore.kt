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
        delay(2000)
        // No navigation set here – will be set by LoadingComplete
    }

    fun resetNavigation() = dispatch(RegisterAction.ResetNavigation)
}