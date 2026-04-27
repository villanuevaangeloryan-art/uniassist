package com.example.uniassist.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.GoogleAuthProvider

data class AuthResult(
    val uid: String,
    val email: String,
    val role: String,
    val firstName: String,
    val lastName: String
)

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // Account with Gmail and Password
    suspend fun signUp(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Sign up failed")
    }

    // Save the extra info (names, role) to Firestore
    suspend fun saveProfile(uid: String, profileData: Map<String, Any>) {
        firestore.collection("users").document(uid).set(profileData).await()
    }

    // UPDATED: Now requires the 'expectedRole' to verify authorization
    suspend fun signIn(email: String, password: String, expectedRole: String): AuthResult {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw Exception("Login failed")
        val doc = firestore.collection("users").document(user.uid).get().await()

        val actualRole = doc.getString("role") ?: "Student"

        // --- THE GATEKEEPER ---
        if (actualRole != expectedRole) {
            // Immediately sign them out of Firebase so they aren't stuck authenticated in the background
            auth.signOut()
            throw Exception("Access Denied: You are registered as a $actualRole, not a $expectedRole.")
        }
        // ----------------------

        return AuthResult(
            uid = user.uid,
            email = email,
            role = actualRole,
            firstName = doc.getString("firstName") ?: "",
            lastName = doc.getString("lastName") ?: ""
        )
    }

    suspend fun signInWithGoogle(idToken: String): String {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user?.email ?: throw Exception("Google Auth Failed")
    }

    suspend fun isUserRegistered(uid: String): Boolean {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            document.exists()
        } catch (e: Exception) {
            false
        }
    }
}