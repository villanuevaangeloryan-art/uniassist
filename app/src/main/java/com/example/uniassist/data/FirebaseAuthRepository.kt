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
    // account with Gmail and Password
    suspend fun signUp(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Sign up failed")
    }

    //sSave the extra info (names, role) to Firestore
    suspend fun saveProfile(uid: String, profileData: Map<String, Any>) {
        firestore.collection("users").document(uid).set(profileData).await()
    }

    // Existing login function
    suspend fun signIn(email: String, password: String): AuthResult {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw Exception("Login failed")
        val doc = firestore.collection("users").document(user.uid).get().await()

        return AuthResult(
            uid = user.uid,
            email = email,
            role = doc.getString("role") ?: "Student",
            firstName = doc.getString("firstName") ?: "",
            lastName = doc.getString("lastName") ?: ""
        )
    }

    suspend fun signInWithGoogle(idToken: String): String {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user?.email ?: throw Exception("Google Auth Failed")
    }
}