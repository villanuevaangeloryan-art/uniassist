package com.example.uniassist.data

import com.example.uniassist.data.db.AnnouncementDao
import com.example.uniassist.data.db.AnnouncementEntity
import com.example.uniassist.ui.screens.Announcement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AnnouncementRepository(private val dao: AnnouncementDao) {

    // All announcements (teacher side — includes archived)
    val announcements: Flow<List<Announcement>> = dao.getAllFlow().map { entities ->
        entities.map { it.toUiModel() }
    }

    // Only non-archived, non-draft announcements (student side)
    val activeAnnouncements: Flow<List<Announcement>> = dao.getActiveFlow().map { entities ->
        entities.map { it.toUiModel() }
    }

    // ── NEW: Draft announcements ──────────────────────────────────────────────
    val draftAnnouncements: Flow<List<Announcement>> = dao.getDraftsFlow().map { entities ->
        entities.map { it.toUiModel() }
    }

    // ── NEW: Archived announcements ───────────────────────────────────────────
    val archivedAnnouncements: Flow<List<Announcement>> = dao.getArchivedFlow().map { entities ->
        entities.map { it.toUiModel() }
    }

    val unreadCount: Flow<Int> = dao.getUnreadCountFlow()

    // PUBLISH (saves as non-draft)
    suspend fun publish(announcement: Announcement, category: String, isPriority: Boolean) {
        dao.insert(announcement.toEntity(category = category, isPriority = isPriority, isDraft = false))
    }

    // ── NEW: Save as draft ────────────────────────────────────────────────────
    suspend fun saveDraft(announcement: Announcement, category: String, isPriority: Boolean) {
        dao.insert(announcement.toEntity(category = category, isPriority = isPriority, isDraft = true))
    }

    // ── NEW: Update existing draft (by id — Room REPLACE handles it via @Insert) ──
    suspend fun updateDraft(announcement: Announcement, category: String, isPriority: Boolean) {
        dao.insert(announcement.toEntity(category = category, isPriority = isPriority, isDraft = true))
    }

    // ── NEW: Publish a draft that already exists in the DB ────────────────────
    suspend fun publishDraft(id: Int) = dao.publishDraft(id)

    // ── NEW: Restore archived announcement to active ──────────────────────────
    suspend fun unarchive(id: Int) = dao.unarchive(id)

    suspend fun markAsRead(id: Int) = dao.markAsRead(id)

    suspend fun markAllAsRead() = dao.markAllAsRead()

    suspend fun delete(entity: AnnouncementEntity) = dao.delete(entity)

    suspend fun archive(id: Int) = dao.archive(id)
}


fun AnnouncementEntity.toUiModel(): Announcement {
    val bullets = if (bulletsRaw.isBlank()) {
        emptyList()
    } else {
        bulletsRaw.split("||").mapNotNull { pair ->
            val parts = pair.split("|")
            if (parts.size == 2) parts[0] to parts[1] else null
        }
    }
    return Announcement(
        id         = id,
        title      = title,
        date       = date,
        content    = content,
        bullets    = bullets,
        footer     = footer,
        category   = category,
        isRead     = isRead,
        isArchived = isArchived,
        isDraft    = isDraft
    )
}

fun Announcement.toEntity(
    category: String,
    isPriority: Boolean,
    isDraft: Boolean = false
): AnnouncementEntity {
    val bulletsRaw = bullets.joinToString("||") { "${it.first}|${it.second}" }
    return AnnouncementEntity(
        id         = id,          // 0 = new insert; non-zero = update via REPLACE
        title      = title,
        date       = date,
        content    = content,
        bulletsRaw = bulletsRaw,
        footer     = footer,
        category   = category,
        isPriority = isPriority,
        isDraft    = isDraft
    )
}