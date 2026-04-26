package com.example.uniassist

import android.app.Application
import com.example.uniassist.ui.screens.NotificationHelper

class UniAssistApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Create the announcements notification channel on app start (required for Android 8+)
        NotificationHelper.createNotificationChannel(this)
    }
}
