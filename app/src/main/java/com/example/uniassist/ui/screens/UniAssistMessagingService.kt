package com.example.uniassist.ui.screens

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class UniAssistMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "New Announcement"
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: ""

        NotificationHelper.createNotificationChannel(this)
        NotificationHelper.showNotification(this, title, body)
    }

    override fun onNewToken(token: String) {
        // Save the refreshed FCM token to Firestore under the current user's document
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .update("fcmToken", token)
    }
}
