package com.example.uniassist.data

import androidx.compose.runtime.mutableStateListOf
import com.example.uniassist.ui.screens.Announcement

// singleton that acts as the single source of truth for announcements.
//  Both the teacher side (publish) and the student side (dashboard) share this
//  same list, when a teacher publishes, the student sees it instantly.


object AnnouncementRepository {

    val announcements = mutableStateListOf(

        Announcement(
            title   = "Campus Operations & Class Suspension",
            date    = "January 19, 2026",
            content = "All on-campus classes are suspended due to bad weather.",
            bullets = listOf(
                "Online Classes:" to "Asynchronous via portal",
                "Offices:"        to "Skeletal workforce",
                "Campus Access:"  to "Essential staff only"
            ),
            footer = "Stay safe everyone!"
        ),
        Announcement(
            title   = "Scholarship Applications Open",
            date    = "January 25, 2026",
            content = "Students with GPA 1.75+ may apply.",
            bullets = listOf(
                "Deadline:" to "Feb 10",
                "Where:"    to "Student Portal",
                "Contact:"  to "Scholarship Office"
            ),
            footer = "Don't miss this opportunity!"
        ),
        Announcement(
            title   = "Library Hours Extended",
            date    = "February 1, 2026",
            content = "The university library will be open until 10 PM during exam week.",
            bullets = listOf(
                "New Hours:"         to "8 AM – 10 PM",
                "Quiet Zones:"       to "Available on 3rd floor",
                "Book Reservations:" to "Online portal"
            ),
            footer = "Good luck with your exams!"
        )
    )

    // Called by the teacher when they tap PUBLISH in CreateAnnouncementScreen
    fun publish(announcement: Announcement) {
        announcements.add(0, announcement)
    }
}