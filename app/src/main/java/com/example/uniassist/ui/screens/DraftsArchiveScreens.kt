package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import com.example.uniassist.ui.theme.DarkGreen

// ─────────────────────────────────────────────────────────────────────────────
// MENU SCREENS  (entry point — "Drafts Menu" / "Archive Menu")
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DraftsMenuScreen(onBackClick: () -> Unit = {}, onNavigate: (String) -> Unit = {}) {
    MenuTemplate(
        title = "Drafts Menu",
        items = listOf("Announcements Draft", "Information Draft"),
        onBackClick = onBackClick,
        onItemClick = onNavigate
    )
}

@Composable
fun ArchiveMenuScreen(onBackClick: () -> Unit = {}, onNavigate: (String) -> Unit = {}) {
    MenuTemplate(
        title = "Archive Menu",
        items = listOf("Announcements Archive", "Information Archive"),
        onBackClick = onBackClick,
        onItemClick = onNavigate
    )
}

@Composable
private fun MenuTemplate(
    title: String,
    items: List<String>,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Scaffold(
        topBar = { DraftArchiveTopBar(title = title, onBackClick = onBackClick) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFB))
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.15f
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items.forEach { item ->
                    MenuCard(title = item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ANNOUNCEMENTS DRAFT LIST SCREEN
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AnnouncementsDraftScreen(
    drafts: List<Announcement>,
    onBackClick: () -> Unit = {},
    onEditDraft: (Announcement) -> Unit = {},
    onPublishDraft: (Announcement) -> Unit = {},
    onDeleteDraft: (Announcement) -> Unit = {}
) {
    Scaffold(
        topBar = { DraftArchiveTopBar(title = "Announcements Draft", onBackClick = onBackClick) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFB))
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.15f
            )

            if (drafts.isEmpty()) {
                EmptyStateMessage(message = "No draft announcements yet.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(drafts, key = { it.id }) { draft ->
                        DraftAnnouncementCard(
                            announcement = draft,
                            onEdit = { onEditDraft(draft) },
                            onPublish = { onPublishDraft(draft) },
                            onDelete = { onDeleteDraft(draft) }
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ANNOUNCEMENTS ARCHIVE LIST SCREEN
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AnnouncementsArchiveScreen(
    archived: List<Announcement>,
    onBackClick: () -> Unit = {},
    onUnarchive: (Announcement) -> Unit = {},
    onDelete: (Announcement) -> Unit = {}
) {
    Scaffold(
        topBar = { DraftArchiveTopBar(title = "Announcements Archive", onBackClick = onBackClick) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFB))
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.15f
            )

            if (archived.isEmpty()) {
                EmptyStateMessage(message = "No archived announcements yet.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(archived, key = { it.id }) { item ->
                        ArchivedAnnouncementCard(
                            announcement = item,
                            onUnarchive = { onUnarchive(item) },
                            onDelete = { onDelete(item) }
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CARDS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DraftAnnouncementCard(
    announcement: Announcement,
    onEdit: () -> Unit,
    onPublish: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPublishDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD4E7D5).copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.25f
            )
            Column(modifier = Modifier.padding(16.dp)) {
                // Title + Category badge row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = announcement.title.ifBlank { "(Untitled)" },
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DarkGreen,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF99C24D).copy(alpha = 0.35f)
                    ) {
                        Text(
                            text = announcement.category.ifBlank { "General" },
                            fontFamily = Poppins,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Date
                Text(
                    text = announcement.date.ifBlank { "No date set" },
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Color(0xFF5A7A65)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Content preview
                Text(
                    text = announcement.content.ifBlank { "No content yet." },
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Color(0xFF3D5A45),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Edit button
                    OutlinedButton(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(DarkGreen, DarkGreen))
                        ),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Edit",
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Publish button
                    Button(
                        onClick = { showPublishDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D6A4F)
                        ),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Publish",
                            modifier = Modifier.size(14.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Publish",
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFFFFEBEE), RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        ConfirmDialog(
            title = "Delete Draft",
            message = "Are you sure you want to permanently delete this draft?",
            confirmLabel = "Delete",
            confirmColor = Color(0xFFD32F2F),
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    // Publish confirmation dialog
    if (showPublishDialog) {
        ConfirmDialog(
            title = "Publish Draft",
            message = "This will publish the announcement and notify students. Continue?",
            confirmLabel = "Publish",
            confirmColor = Color(0xFF2D6A4F),
            onConfirm = {
                showPublishDialog = false
                onPublish()
            },
            onDismiss = { showPublishDialog = false }
        )
    }
}

@Composable
private fun ArchivedAnnouncementCard(
    announcement: Announcement,
    onUnarchive: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUnarchiveDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8).copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.18f
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = announcement.title.ifBlank { "(Untitled)" },
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF3D3D3D),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFBDBDBD).copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = announcement.category.ifBlank { "General" },
                            fontFamily = Poppins,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF616161),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = announcement.date.ifBlank { "No date" },
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Color(0xFF757575)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = announcement.content.ifBlank { "No content." },
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Color(0xFF616161),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Unarchive button
                    Button(
                        onClick = { showUnarchiveDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Unarchive,
                            contentDescription = "Restore",
                            modifier = Modifier.size(14.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Restore",
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFFFFEBEE), RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showUnarchiveDialog) {
        ConfirmDialog(
            title = "Restore Announcement",
            message = "Restore this announcement to active? It will be visible to students again.",
            confirmLabel = "Restore",
            confirmColor = Color(0xFF2D6A4F),
            onConfirm = {
                showUnarchiveDialog = false
                onUnarchive()
            },
            onDismiss = { showUnarchiveDialog = false }
        )
    }

    if (showDeleteDialog) {
        ConfirmDialog(
            title = "Delete Announcement",
            message = "Permanently delete this archived announcement? This cannot be undone.",
            confirmLabel = "Delete",
            confirmColor = Color(0xFFD32F2F),
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SHARED COMPONENTS (internal to this file)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DraftArchiveTopBar(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))
                )
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkGreen,
                    modifier = Modifier.size(26.dp)
                )
            }
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = DarkGreen,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun MenuCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(344.dp)
            .height(86.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD4E7D5).copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.35f
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Archive,
                contentDescription = null,
                tint = Color(0xFFAECFB4),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = Color(0xFF7AA685),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    confirmColor: Color,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF5FBF5),
        title = {
            Text(
                text = title,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkGreen
            )
        },
        text = {
            Text(
                text = message,
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Color(0xFF3D5A45)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = confirmColor)
            ) {
                Text(
                    text = confirmLabel,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontFamily = Poppins,
                    color = Color(0xFF5A7A65)
                )
            }
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun PreviewDraftsMenu() {
    DraftsMenuScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewArchiveMenu() {
    ArchiveMenuScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewAnnouncementsDraftScreen() {
    val sampleDrafts = listOf(
        Announcement(
            id = 1, title = "Enrollment Reminder", date = "2025-06-01",
            content = "Please complete your enrollment before the deadline.",
            bullets = emptyList(), footer = "", category = "Academic",
            isArchived = false, isDraft = true
        ),
        Announcement(
            id = 2, title = "Campus Clean-up Drive", date = "2025-06-10",
            content = "All students are encouraged to join the clean-up activity.",
            bullets = emptyList(), footer = "", category = "Events",
            isArchived = false, isDraft = true
        )
    )
    AnnouncementsDraftScreen(drafts = sampleDrafts)
}

@Preview(showBackground = true)
@Composable
fun PreviewAnnouncementsArchiveScreen() {
    val sampleArchived = listOf(
        Announcement(
            id = 3, title = "Old Bulletin Notice", date = "2025-04-10",
            content = "This announcement has been archived.",
            bullets = emptyList(), footer = "", category = "General",
            isArchived = true, isDraft = false
        )
    )
    AnnouncementsArchiveScreen(archived = sampleArchived)
}