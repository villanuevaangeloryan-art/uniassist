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
            _state.update { current -> reducer.reduce(current, action) }

            when (action) {
                is LoginAction.LoginButtonClicked -> {
                    if (_state.value.isFormValid) performLogin(action.expectedRole)
                }
                is LoginAction.GoogleSignInClicked -> {
                    performGoogleLogin(action.idToken)
                }
                else -> {}
            }
        }
    }

    private suspend fun performLogin(expectedRole: String) {
        _state.update { it.copy(isLoading = true, authError = "") }
        try {
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
            saveFcmToken(authResult.uid)
            if (authResult.role == "Student") {
                FirebaseMessaging.getInstance().subscribeToTopic("announcements")
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(isLoading = false, authError = e.message ?: "Login failed. Check your connection.")
            }
        }
    }

    // NEW: handles Google sign-in flow
    private suspend fun performGoogleLogin(idToken: String) {
        _state.update { it.copy(isLoading = true, authError = "") }
        try {
            val email = authRepository.signInWithGoogle(idToken)

            // Fetch the profile from Firestore to get name and role
            val firestore = FirebaseFirestore.getInstance()
            val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                ?: throw Exception("Google sign-in failed")

            val doc = firestore.collection("users").document(uid).get().await()

            _state.update {
                it.copy(
                    isLoading = false,
                    userRole = doc.getString("role") ?: "Student",
                    firstName = doc.getString("firstName") ?: "",
                    lastName = doc.getString("lastName") ?: "",
                    navigateToDashboard = true
                )
            }
            saveFcmToken(uid)
        } catch (e: Exception) {
            _state.update {
                it.copy(isLoading = false, authError = e.message ?: "Google sign-in failed.")
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
        } catch (_: Exception) {}
    }
}