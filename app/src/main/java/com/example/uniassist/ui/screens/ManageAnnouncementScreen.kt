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

private val CatEvent      = Color(0xFFF57C00)
private val CatEnrollment = Color(0xFFD32F2F)
private val CatInfo       = Color(0xFFF57C00)
private val CatEmergency  = Color(0xFFD32F2F)
private val CatAcademic   = Color(0xFF1565C0)
private val CatGeneral    = Color(0xFF2E7D32)

// ── Card model ────────────────────────────────────────────────────────────────
// CHANGE: Made internal (not private) so it can be created from outside this file.
data class ManageScreenAnnouncement(
    val id: Int,
    val title: String,
    val category: String,
    val categoryIcon: ImageVector,
    val categoryColor: Color,
    val timePosted: String,
    val statusText: String,
    val statusColor: Color,
    val description: String? = null
)

// ── Helper: converts a NewAnnouncement (from CreateAnnouncementScreen) into a
//   ManageScreenAnnouncement ready to be displayed in the list.
fun NewAnnouncement.toManageCard(id: Int): ManageScreenAnnouncement {
    val (icon, color) = when (category.lowercase()) {
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
    return ManageScreenAnnouncement(
        id          = id,
        title       = title,
        category    = category,
        categoryIcon  = icon,
        categoryColor = color,
        timePosted  = "just now",
        statusText  = "Active",
        statusColor = StatusActive,
        description = message.ifBlank { null }
    )
}

// ── Screen ────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAnnouncementsScreen(
    onBackClick: () -> Unit = {},
    onCreateClick: () -> Unit = {},
    publishedAnnouncements: List<ManageScreenAnnouncement> = emptyList()
) {
    var searchQuery by remember { mutableStateOf("") }
    var profileMenuExpanded by remember { mutableStateOf(false) }


    val staticAnnouncements = remember {
        listOf(
            ManageScreenAnnouncement(
                id = 1,
                title = "Campus Event Tomorrow",
                category = "Event",
                categoryIcon = Icons.Outlined.Event,
                categoryColor = CatEvent,
                timePosted = "posted 1h ago",
                statusText = "active",
                statusColor = StatusActive
            ),
            ManageScreenAnnouncement(
                id = 2,
                title = "Reminder: Enrollment Deadline",
                category = "Enrollment",
                categoryIcon = Icons.Outlined.NotificationsActive,
                categoryColor = CatEnrollment,
                timePosted = "posted 2 days ago",
                statusText = "Active",
                statusColor = StatusActive,
                description = "Final day to enroll for the semester is May 15th!"
            ),
            ManageScreenAnnouncement(
                id = 3,
                title = "Library Closed for Maintenance",
                category = "Info",
                categoryIcon = Icons.Outlined.Event,
                categoryColor = CatInfo,
                timePosted = "posted 5 days ago",
                statusText = "Ending soon",
                statusColor = StatusEndingSoon
            ),
            ManageScreenAnnouncement(
                id = 4,
                title = "Campus Event Tomorrow",
                category = "Event",
                categoryIcon = Icons.Outlined.Event,
                categoryColor = CatEvent,
                timePosted = "posted 1h ago",
                statusText = "Expired",
                statusColor = StatusExpired
            )
        )
    }


    val allAnnouncements = publishedAnnouncements + staticAnnouncements

    // Filter by search query
    val displayed = if (searchQuery.isBlank()) {
        allAnnouncements
    } else {
        allAnnouncements.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = BackgroundCream,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateClick,
                containerColor = Color(0xFF2E5E3E),
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "New Announcement",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Top Gradient Bar ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF7CA786), Color(0xFFE2F1C6)))
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GreenDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = "Announcements",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GreenDark
                    )
                    Box {
                        IconButton(onClick = { profileMenuExpanded = true }) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .border(2.dp, GreenDark, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = GreenDark,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = profileMenuExpanded,
                            onDismissRequest = { profileMenuExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Drafts", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Description, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Archive, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Search Bar ─────────────────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                placeholder = {
                    Text(
                        "Search announcements . . .",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    ) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search", tint = GreenDark)
                        Spacer(modifier = Modifier.width(12.dp))
                        VerticalDivider(
                            modifier = Modifier.height(24.dp),
                            color = GreenMedium.copy(alpha = 0.5f),
                            thickness = 1.dp
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenMedium,
                    unfocusedBorderColor = GreenMedium.copy(alpha = 0.6f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Filter Chips ───────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ManageAnnouncementsFilterChip(
                    "Sort by",
                    Icons.AutoMirrored.Outlined.Sort,
                    listOf("Newest", "Oldest", "Archive")
                )
                ManageAnnouncementsStatusChip("Filter by", Icons.Outlined.FilterList)
                ManageAnnouncementsDateChip("Date", Icons.Outlined.CalendarToday)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Announcements List ─────────────────────────────────────────────
            // CHANGE: Shows "displayed" list (merged + filtered) instead of the
            // old hardcoded local val.
            if (displayed.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) "No announcements yet.\nTap + New Announcement to create one."
                        else "No results for \"$searchQuery\".",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(displayed, key = { it.id }) { announcement ->
                        ManageAnnouncementCard(announcement)
                    }
                }
            }
        }
    }
}

// ── Card composable ───────────────────────────────────────────────────────────
@Composable
private fun ManageAnnouncementCard(announcement: ManageScreenAnnouncement) {
    var moreExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp, top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = announcement.title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = GreenDark,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box {
                    IconButton(
                        onClick = { moreExpanded = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
                    }
                    DropdownMenu(
                        expanded = moreExpanded,
                        onDismissRequest = { moreExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit", fontFamily = Poppins, fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(16.dp)) },
                            onClick = { moreExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", fontFamily = Poppins, fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Outlined.Delete, null, modifier = Modifier.size(16.dp)) },
                            onClick = { moreExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Outlined.Archive, null, modifier = Modifier.size(16.dp)) },
                            onClick = { moreExpanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = announcement.categoryIcon,
                    contentDescription = null,
                    tint = announcement.categoryColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = announcement.category,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = announcement.categoryColor
                )
                Spacer(Modifier.width(8.dp))
                Text("|", color = Color.LightGray, fontSize = 12.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = announcement.timePosted,
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.weight(1f))
                if (announcement.statusText.lowercase() != "active") {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(announcement.statusColor)
                    )
                    Spacer(Modifier.width(4.dp))
                }
                Text(
                    text = announcement.statusText,
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
                if (announcement.statusText.lowercase() == "active") {
                    Spacer(Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(announcement.statusColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (announcement.description != null) {
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 1.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F9F8))
                        .padding(16.dp)
                ) {
                    Text(
                        text = announcement.description,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

// ── Filter / Sort chip helpers (unchanged from original) ──────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageAnnouncementsFilterChip(text: String, icon: ImageVector, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(16.dp))
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(icon, null, tint = GreenDark, modifier = Modifier.size(14.dp))
                Text(text, fontFamily = Poppins, fontSize = 12.sp, color = GreenDark)
                Icon(Icons.Default.ArrowDropDown, null, tint = GreenDark, modifier = Modifier.size(14.dp))
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontFamily = Poppins, fontSize = 12.sp) },
                    onClick = { expanded = false }
                )
            }
        }
    }
}

@Composable
private fun ManageAnnouncementsStatusChip(text: String, icon: ImageVector) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("All", "Active", "Ending soon", "Expired")
    Box {
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(16.dp))
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(icon, null, tint = GreenDark, modifier = Modifier.size(14.dp))
                Text(text, fontFamily = Poppins, fontSize = 12.sp, color = GreenDark)
                Icon(Icons.Default.ArrowDropDown, null, tint = GreenDark, modifier = Modifier.size(14.dp))
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            statuses.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status, fontFamily = Poppins, fontSize = 12.sp) },
                    onClick = { expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageAnnouncementsDateChip(text: String, icon: ImageVector) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf(text) }

    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(16.dp))
            .clickable { showDatePicker = true },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, tint = GreenDark, modifier = Modifier.size(14.dp))
            Text(selectedDateText, fontFamily = Poppins, fontSize = 12.sp, color = GreenDark)
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        selectedDateText = formatter.format(Date(millis))
                    }
                }) { Text("OK", fontFamily = Poppins, color = GreenDark) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", fontFamily = Poppins, color = Color.Gray)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = GreenMedium,
                    todayDateBorderColor = GreenDark,
                    todayContentColor = GreenDark
                )
            )
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ManageAnnouncementsPreview() {
    MaterialTheme {
        ManageAnnouncementsScreen()
    }
}