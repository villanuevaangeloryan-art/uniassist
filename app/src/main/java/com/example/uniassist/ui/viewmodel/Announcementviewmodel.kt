package com.example.uniassist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uniassist.data.AnnouncementRepository
import com.example.uniassist.data.db.AnnouncementEntity
import com.example.uniassist.ui.screens.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    // Only non-archived, non-draft announcements (student dashboard)
    val activeAnnouncements: StateFlow<List<Announcement>> = repository.activeAnnouncements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // ── NEW: Draft announcements ──────────────────────────────────────────────
    val draftAnnouncements: StateFlow<List<Announcement>> = repository.draftAnnouncements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // ── NEW: Archived announcements ───────────────────────────────────────────
    val archivedAnnouncements: StateFlow<List<Announcement>> = repository.archivedAnnouncements
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

    // Publish immediately (not a draft)
    fun publish(announcement: Announcement, category: String, isPriority: Boolean) {
        viewModelScope.launch {
            repository.publish(announcement, category, isPriority)
            triggerNotification(
                title = announcement.title,
                body  = announcement.content.ifBlank { "New announcement posted." }
            )
        }
    }

    // ── NEW: Save a new draft ─────────────────────────────────────────────────
    fun saveDraft(announcement: Announcement, category: String, isPriority: Boolean) {
        viewModelScope.launch {
            repository.saveDraft(announcement, category, isPriority)
        }
    }

    // ── NEW: Update an existing draft (edit screen) ───────────────────────────
    fun updateDraft(announcement: Announcement, category: String, isPriority: Boolean) {
        viewModelScope.launch {
            repository.updateDraft(announcement, category, isPriority)
        }
    }

    // ── NEW: Publish a draft that already exists in DB ────────────────────────
    fun publishDraft(announcement: Announcement) {
        viewModelScope.launch {
            repository.publishDraft(announcement.id)
            triggerNotification(
                title = announcement.title,
                body  = announcement.content.ifBlank { "New announcement posted." }
            )
        }
    }

    // ── NEW: Restore archived announcement ────────────────────────────────────
    fun unarchive(id: Int) {
        viewModelScope.launch {
            repository.unarchive(id)
        }
    }

    // ── NEW: Delete a draft or archived announcement ──────────────────────────
    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            val entity = AnnouncementEntity(
                id         = announcement.id,
                title      = announcement.title,
                date       = announcement.date,
                content    = announcement.content,
                bulletsRaw = announcement.bullets.joinToString("||") { "${it.first}|${it.second}" },
                footer     = announcement.footer,
                category   = announcement.category,
                isPriority = false,
                isArchived = announcement.isArchived,
                isDraft    = announcement.isDraft
            )
            repository.delete(entity)
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
        viewModelScope.launch { repository.markAsRead(id) }
    }

    fun markAllAsRead() {
        viewModelScope.launch { repository.markAllAsRead() }
    }

    fun archive(id: Int) {
        viewModelScope.launch { repository.archive(id) }
    }

    class Factory(private val repository: AnnouncementRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AnnouncementViewModel(repository) as T
    }
}