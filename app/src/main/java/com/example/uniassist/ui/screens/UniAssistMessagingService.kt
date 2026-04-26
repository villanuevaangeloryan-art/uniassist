package com.example.uniassist.ui.screens

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
        // Token refresh — handled in a later commit
    }
}
