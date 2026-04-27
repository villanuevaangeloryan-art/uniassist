package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.AnnouncementRepository
import com.example.uniassist.ui.screens.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ── ViewModel
class AnnouncementViewModel(
    private val repository: AnnouncementRepository
) : ViewModel() {

    // All announcements (teacher side — includes archived)
    val announcements: StateFlow<List<Announcement>> = repository.announcements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Only non-archived announcements (student dashboard)
    val activeAnnouncements: StateFlow<List<Announcement>> = repository.activeAnnouncements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Unread count
    val unreadCount: StateFlow<Int> = repository.unreadCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )


    fun publish(announcement: Announcement, category: String, isPriority: Boolean) {
        viewModelScope.launch {
            repository.publish(announcement, category, isPriority)
            // Write a notification trigger to Firestore so a Cloud Function
            // (or any server-side listener) can fan-out an FCM topic message
            // to all subscribed students.
            triggerNotification(
                title = announcement.title,
                body  = announcement.content.ifBlank { "New announcement posted." }
            )
        }
    }

    private fun triggerNotification(title: String, body: String) {
        val payload = mapOf(
            "title"     to title,
            "body"      to body,
            "topic"     to "announcements",
            "timestamp" to System.currentTimeMillis()
        )
        FirebaseFirestore.getInstance()
            .collection("notification_triggers")
            .add(payload)
    }

    fun markAsRead(id: Int) {
        viewModelScope.launch {
            repository.markAsRead(id)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead()
        }
    }

    fun archive(id: Int) {
        viewModelScope.launch {
            repository.archive(id)
        }
    }

    class Factory(private val repository: AnnouncementRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AnnouncementViewModel(repository) as T
    }
}