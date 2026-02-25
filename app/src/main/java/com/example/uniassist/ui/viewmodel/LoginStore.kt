package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.domain.reducers.LoginReducer
import com.example.uniassist.domain.state.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginStore : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state
    private val reducer = LoginReducer()

    fun dispatch(action: LoginAction) {
        viewModelScope.launch {
            _state.update { current ->
                reducer.reduce(current, action)
            }
            when (action) {
                is LoginAction.LoginButtonClicked -> {
                    if (_state.value.isFormValid) performLogin()
                }
                else -> {}
            }
        }
    }

    private suspend fun performLogin() {
        _state.update { it.copy(isLoading = true) }
        delay(2000)
        _state.update { it.copy(isLoading = false, navigateToDashboard = true) }
    }

    fun resetNavigation() = dispatch(LoginAction.ResetNavigation)
}