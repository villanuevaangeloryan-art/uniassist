package com.example.uniassist.ui.screens

// ── Shared UI model used across all announcement screens ─────────────────────
data class Announcement(
    val id: Int = 0,
    val title: String,
    val date: String,
    val content: String,
    val bullets: List<Pair<String, String>>,
    val footer: String,
    val category: String,
    val isRead: Boolean = false,
    val isArchived: Boolean = false,
    val isDraft: Boolean = false          // ← NEW: true when saved as draft
)