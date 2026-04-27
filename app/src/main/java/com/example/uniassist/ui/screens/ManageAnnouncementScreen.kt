package com.example.uniassist.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ── Colors ────────────────────────────────────────────────────────────────────
private val GreenDark       = Color(0xFF1B5E20)
private val GreenMedium     = Color(0xFF56A566)
private val BackgroundCream = Color(0xFFFDFDF8)

private val StatusActive     = Color(0xFF66DE74)
private val StatusEndingSoon = Color(0xFFFFD54F)
private val StatusExpired    = Color(0xFFFF1744)

private val CatEvent     = Color(0xFFF57C00)
private val CatEmergency = Color(0xFFD32F2F)
private val CatAcademic  = Color(0xFF1565C0)
private val CatGeneral   = Color(0xFF2E7D32)

// ── Card model ────────────────────────────────────────────────────────────────
data class ManageScreenAnnouncement(
    val id: Int,
    val title: String,
    val category: String,
    val categoryIcon: ImageVector,
    val categoryColor: Color,
    val timePosted: String,
    val statusText: String,
    val statusColor: Color,
    val description: String? = null,
    val isArchived: Boolean = false,
    val isRead: Boolean = false
)

fun Announcement.toManageCard(): ManageScreenAnnouncement {
    val (icon, color) = categoryIconAndColor(category)
    return ManageScreenAnnouncement(
        id            = id,
        title         = title,
        category      = category,
        categoryIcon  = icon,
        categoryColor = color,
        timePosted    = date,
        statusText    = if (isArchived) "Archived" else "Active",
        statusColor   = if (isArchived) Color(0xFF9E9E9E) else StatusActive,
        description   = content.ifBlank { null },
        isArchived    = isArchived,
        isRead        = isRead
    )
}

private fun categoryIconAndColor(category: String): Pair<ImageVector, Color> =
    when (category.lowercase()) {
        "exams", "enrollment", "deadlines", "scholarship updates" ->
            Icons.Outlined.School to CatAcademic
        "campus festival", "org activities", "sports fest" ->
            Icons.Outlined.Event to CatEvent
        "office hours", "policy reminders", "general updates" ->
            Icons.Outlined.Info to CatGeneral
        "class suspension", "typhoon alerts", "safety notices" ->
            Icons.Outlined.Warning to CatEmergency
        else -> Icons.Outlined.Campaign to GreenMedium
    }

// ── Screen ────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAnnouncementsScreen(
    onBackClick: () -> Unit = {},
    onCreateClick: () -> Unit = {},
    dbAnnouncements: List<ManageScreenAnnouncement> = emptyList(),
    onArchive: (Int) -> Unit = {},
    onUnarchive: (Int) -> Unit = {},
    onDelete: (ManageScreenAnnouncement) -> Unit = {},
    onEditNavigate: (Int) -> Unit = {}
) {
    var searchQuery    by remember { mutableStateOf("") }
    var profileMenuExpanded by remember { mutableStateOf(false) }

    var selectedSort   by remember { mutableStateOf("Newest") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedDate   by remember { mutableStateOf<String?>(null) }

    var announcementToDelete by remember { mutableStateOf<ManageScreenAnnouncement?>(null) }

    val displayed = remember(dbAnnouncements, searchQuery, selectedSort, selectedFilter, selectedDate) {
        var result = if (searchQuery.isBlank()) dbAnnouncements
        else dbAnnouncements.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true)
        }
        result = when (selectedFilter) {
            "Active"       -> result.filter { !it.isArchived && it.statusText == "Active" }
            "Archived"     -> result.filter { it.isArchived }
            "Ending soon"  -> result.filter { it.statusText == "Ending soon" }
            "Expired"      -> result.filter { it.statusText == "Expired" }
            else           -> result
        }
        if (!selectedDate.isNullOrBlank()) {
            result = result.filter { it.timePosted == selectedDate }
        }
        result = when (selectedSort) {
            "Oldest"   -> result.sortedBy { it.id }
            "Archived" -> result.sortedByDescending { it.isArchived }
            else       -> result.sortedByDescending { it.id }
        }
        result
    }

    Scaffold(
        containerColor               = BackgroundCream,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick        = onCreateClick,
                containerColor = Color(0xFF2E5E3E),
                contentColor   = Color.White,
                shape          = RoundedCornerShape(12.dp),
                modifier       = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("New Announcement", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Box(
                modifier = Modifier.fillMaxWidth().height(65.dp)
                    .background(Brush.horizontalGradient(listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))))
            ) {
                Row(
                    modifier              = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = GreenDark, modifier = Modifier.size(28.dp))
                    }
                    Text("Announcements", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = GreenDark)
                    Box {
                        IconButton(onClick = { profileMenuExpanded = true }) {
                            Box(modifier = Modifier.size(34.dp).border(2.dp, GreenDark, CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AccountCircle, "Profile", tint = GreenDark, modifier = Modifier.size(30.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Announcements List
            if (displayed.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    Text(text = "No announcements match your search.", fontFamily = Poppins, fontSize = 14.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding      = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(displayed, key = { it.id }) { announcement ->
                        ManageAnnouncementCard(
                            announcement    = announcement,
                            onArchive       = onArchive,
                            onUnarchive     = onUnarchive,
                            onDeleteRequest = { announcementToDelete = it },
                            onEditRequest   = onEditNavigate // Directly pass the function
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ManageAnnouncementCard(
    announcement    : ManageScreenAnnouncement,
    onArchive       : (Int) -> Unit = {},
    onUnarchive     : (Int) -> Unit = {},
    onDeleteRequest : (ManageScreenAnnouncement) -> Unit = {},
    onEditRequest   : (Int) -> Unit = {}
) {
    var moreExpanded by remember { mutableStateOf(false) }

    Card(
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        border    = BorderStroke(
            width = 1.dp,
            color = if (!announcement.isRead && !announcement.isArchived) Color(0xFF4CAF50).copy(alpha = 0.5f) else Color.LightGray.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text       = announcement.title,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 17.sp,
                        color      = GreenDark,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.weight(1f, fill = false)
                    )
                    if (!announcement.isRead && !announcement.isArchived) {
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE9B44C)) {
                            Text("NEW", fontFamily = Poppins, fontSize = 10.sp, color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
                Box {
                    IconButton(onClick = { moreExpanded = true }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
                    }
                    DropdownMenu(expanded = moreExpanded, onDismissRequest = { moreExpanded = false }, modifier = Modifier.background(Color.White)) {
                        DropdownMenuItem(
                            text        = { Text("Edit", fontFamily = Poppins, fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(16.dp)) },
                            onClick     = { moreExpanded = false; onEditRequest(announcement.id) }
                        )
                        DropdownMenuItem(
                            text        = { Text("Delete", fontFamily = Poppins, fontSize = 12.sp, color = Color(0xFFD32F2F)) },
                            leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(16.dp)) },
                            onClick     = { moreExpanded = false; onDeleteRequest(announcement) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(announcement.categoryIcon, contentDescription = null, tint = announcement.categoryColor, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(announcement.category, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = announcement.categoryColor)
                Spacer(Modifier.width(8.dp))
                Text("|", color = Color.LightGray, fontSize = 12.sp)
                Spacer(Modifier.width(8.dp))
                Text(announcement.timePosted, fontFamily = Poppins, fontSize = 11.sp, color = Color.Gray)
            }

            if (announcement.description != null) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF7F9F8)).padding(16.dp)) {
                    Text(announcement.description, fontFamily = Poppins, fontSize = 14.sp, color = Color.Black)
                }
            }

            // Read Status Indicator for Teacher
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { /* Teacher preview or detailed view */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (announcement.isRead) Color(0xFFDDE9E1) else GreenDark
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = if (announcement.isRead) Icons.Default.DoneAll else Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = if (announcement.isRead) GreenDark else Color.White
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (announcement.isRead) "Viewed" else "Read",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (announcement.isRead) GreenDark else Color.White
                    )
                }
            }
        }
    }
}
