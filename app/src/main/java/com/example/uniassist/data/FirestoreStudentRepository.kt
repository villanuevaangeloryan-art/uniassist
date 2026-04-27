package com.example.uniassist.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreStudentRepository {
    private val db = FirebaseFirestore.getInstance()

    // callbackFlow allows us to convert Firebase's callback system into a modern Kotlin Flow
    fun getRegisteredStudents(): Flow<List<StudentModel>> = callbackFlow {

        // IMPORTANT: Change "users" to match your exact Firestore collection name!
        // We filter where "role" equals "Student" so Teachers/Admins don't show up in the list.
        val subscription = db.collection("users")
            .whereEqualTo("role", "Student")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Close the flow if there is a Firebase error
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val students = snapshot.documents.mapNotNull { doc ->
                        // Convert the Firestore document into our Kotlin object
                        val student = doc.toObject(StudentModel::class.java)
                        // Attach the unique document ID to our object
                        student?.copy(id = doc.id)
                    }
                    trySend(students) // Send the list to the ViewModel
                }
            }

        // This removes the Firebase listener when the screen is closed to save battery/data
        awaitClose { subscription.remove() }
    }
}