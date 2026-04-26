package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.FirebaseAuthRepository
import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.domain.reducer.LoginReducer
import com.example.uniassist.domain.state.LoginState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginStore(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state
    private val reducer = LoginReducer()

    fun dispatch(action: LoginAction) {
        viewModelScope.launch {
            _state.update { current ->
                reducer.reduce(current, action)
            }

            when (action) {
                // 1. Catch the expectedRole from the action
                is LoginAction.LoginButtonClicked -> {
                    if (_state.value.isFormValid) performLogin(action.expectedRole)
                }
                else -> {}
            }
        }
    }

    // 2. Add expectedRole as a parameter
    private suspend fun performLogin(expectedRole: String) {
        _state.update { it.copy(isLoading = true, authError = "") }

        try {
            // 3. Pass expectedRole into the repository!
            val authResult = authRepository.signIn(
                email = _state.value.email,
                password = _state.value.password,
                expectedRole = expectedRole
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    userRole = authResult.role,
                    firstName = authResult.firstName,
                    lastName = authResult.lastName,
                    navigateToDashboard = true
                )
            }

            // Retrieve current FCM token and save it to Firestore after login
            saveFcmToken(authResult.uid)
        } catch (e: Exception) {
            // If the gatekeeper throws an error, it gets caught right here!
            _state.update {
                it.copy(
                    isLoading = false,
                    authError = e.message ?: "Login failed. Check your connection."
                )
            }
        }
    }

    fun resetNavigation() = dispatch(LoginAction.ResetNavigation)

    private suspend fun saveFcmToken(uid: String) {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .update("fcmToken", token)
        } catch (_: Exception) {
            // Non-critical — token will be saved on next onNewToken callback
        }
    }
}