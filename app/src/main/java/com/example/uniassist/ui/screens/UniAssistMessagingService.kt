package com.example.uniassist.ui.screens

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class UniAssistMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Prefer notification payload, fall back to data payload
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "New Announcement"
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: "You have a new announcement."

        // Ensure channel exists then show the notification
        NotificationHelper.createNotificationChannel(this)
        NotificationHelper.showNotification(
            context = this,
            title   = title,
            body    = body
        )
    }

    override fun onNewToken(token: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .update("fcmToken", token)
    }
}
