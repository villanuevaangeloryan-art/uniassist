package com.example.uniassist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.uniassist.ui.theme.*

// ── Colors for consistency ──────────────────────────────────────────────────
private val BackgroundCream = Color(0xFFFDFDF8)

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
        topBar = { DraftArchiveTopBar(title = title, onBackClick = onBackClick) },
        containerColor = BackgroundCream
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.12f
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
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
        topBar = { DraftArchiveTopBar(title = "Announcements Draft", onBackClick = onBackClick) },
        containerColor = BackgroundCream
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.12f
            )

            if (drafts.isEmpty()) {
                EmptyStateMessage(message = "No draft announcements yet.", icon = Icons.Default.EditNote)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
        topBar = { DraftArchiveTopBar(title = "Announcements Archive", onBackClick = onBackClick) },
        containerColor = BackgroundCream
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.12f
            )

            if (archived.isEmpty()) {
                EmptyStateMessage(message = "No archived announcements yet.", icon = Icons.Default.Archive)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title + Category badge row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.title.ifBlank { "(Untitled Draft)" },
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = DarkGreenText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFE9B44C).copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Color(0xFFE9B44C).copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "DRAFT",
                        fontFamily = Poppins,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB8860B),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Date & Category
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = announcement.category.ifBlank { "General" },
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF56A566)
                )
                Text(
                    text = " • ",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
                Text(
                    text = announcement.date.ifBlank { "Last edited just now" },
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Content preview
            Text(
                text = announcement.content.ifBlank { "No content description added yet." },
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit button
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreenText),
                    border = BorderStroke(1.dp, DarkGreenText),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Edit, "Edit", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Edit", fontFamily = Poppins, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                // Publish button
                Button(
                    onClick = { showPublishDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Send, "Publish", modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Publish", fontFamily = Poppins, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }

                // Delete button
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                }
            }
        }
    }

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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = "ARCHIVED",
                        fontFamily = Poppins,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Posted on: ${announcement.date}",
                fontFamily = Poppins,
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = announcement.content,
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showUnarchiveDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Unarchive, "Restore", modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Restore", fontFamily = Poppins, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }

                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                }
            }
        }
    }

    if (showUnarchiveDialog) {
        ConfirmDialog(
            title = "Restore Announcement",
            message = "Restore this announcement to active status? It will be visible to students again.",
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
            title = "Delete Permanently",
            message = "This action cannot be undone. Delete this announcement forever?",
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
// SHARED COMPONENTS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DraftArchiveTopBar(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(Brush.horizontalGradient(listOf(HeaderGreenStart, HeaderGreenEnd)))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DarkGreenText, modifier = Modifier.size(28.dp))
            }
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenText,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier.size(34.dp).border(2.dp, DarkGreenText, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccountCircle, "Profile", tint = DarkGreenText, modifier = Modifier.size(30.dp))
            }
        }
    }
}

@Composable
private fun MenuCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.08f
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF1F8F1),
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = if (title.contains("Draft")) Icons.Default.EditNote else Icons.Default.Archive,
                        contentDescription = null,
                        tint = DarkGreenText,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreenText,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = DarkGreenText, modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
private fun EmptyStateMessage(message: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFAECFB4),
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontFamily = Poppins,
                fontSize = 15.sp,
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
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        title = {
            Text(text = title, fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreenText)
        },
        text = {
            Text(text = message, fontFamily = Poppins, fontSize = 14.sp, color = Color.Gray)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = confirmColor)
            ) {
                Text(text = confirmLabel, fontFamily = Poppins, fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", fontFamily = Poppins, fontWeight = FontWeight.Medium, color = Color.Gray)
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
fun PreviewAnnouncementsDraftScreen() {
    val sampleDrafts = listOf(
        Announcement(
            id = 1, title = "Enrollment Reminder", date = "2025-06-01",
            content = "Please complete your enrollment before the deadline.",
            bullets = emptyList(), footer = "", category = "Academic",
            isArchived = false, isDraft = true
        )
    )
    AnnouncementsDraftScreen(drafts = sampleDrafts)
}
