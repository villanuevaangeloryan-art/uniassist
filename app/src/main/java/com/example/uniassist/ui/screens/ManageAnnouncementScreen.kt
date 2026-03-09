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

// ── Colors from Provided Code ─────────────────────────────────────────────────
private val GreenDark       = Color(0xFF1B5E20)
private val GreenMedium     = Color(0xFF56A566)
private val BackgroundCream = Color(0xFFFDFDF8)

// Status Colors
private val StatusActive     = Color(0xFF66DE74)
private val StatusEndingSoon = Color(0xFFFFD54F)
private val StatusExpired    = Color(0xFFFF1744)

// Category Colors
private val CatEvent      = Color(0xFFF57C00)
private val CatEnrollment = Color(0xFFD32F2F)
private val CatInfo       = Color(0xFFF57C00)

// ── Dummy Data Model ──────────────────────────────────────────────────────────
private data class ManageScreenAnnouncement(
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

// ── Manage Announcements Screen ───────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAnnouncementsScreen(
    onBackClick: () -> Unit = {},
    onCreateClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var profileMenuExpanded by remember { mutableStateOf(false) }

    // Dummy List to match the image exactly
    val announcements = listOf(
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
                Text("New Announcement", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
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
                        Brush.horizontalGradient(
                            listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))
                        )
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

                        // Profile Dropdown
                        DropdownMenu(
                            expanded = profileMenuExpanded,
                            onDismissRequest = { profileMenuExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Drafts", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Outlined.Archive, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
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
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = GreenDark
                        )
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

            // ── Filter Chips Row ───────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ManageAnnouncementsFilterChip("Sort by", Icons.AutoMirrored.Outlined.Sort, listOf("Newest", "Oldest", "Archive"))
                ManageAnnouncementsStatusChip("Filter by", Icons.Outlined.FilterList)
                // NEW CALENDAR CHIP
                ManageAnnouncementsDateChip("Date", Icons.Outlined.CalendarToday)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Announcements List ─────────────────────────────────────────────
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(announcements) { announcement ->
                    ManageAnnouncementCard(announcement)
                }
            }
        }
    }
}

// ── Components ────────────────────────────────────────────────────────────────

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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.width(4.dp))
                Text(text, fontFamily = Poppins, fontSize = 11.sp, color = Color.DarkGray)
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.width(4.dp))
                Text(text, fontFamily = Poppins, fontSize = 11.sp, color = Color.DarkGray)
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color(0xFFF0F4F7))
                .clip(RoundedCornerShape(8.dp))
        ) {
            ManageAnnouncementsStatusItem("active", StatusActive) { expanded = false }
            ManageAnnouncementsStatusItem("ending soon", StatusEndingSoon) { expanded = false }
            ManageAnnouncementsStatusItem("expired", StatusExpired) { expanded = false }
        }
    }
}

@Composable
private fun ManageAnnouncementsStatusItem(label: String, color: Color, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, fontFamily = Poppins, fontSize = 12.sp, color = Color.DarkGray)
            }
        },
        onClick = onClick
    )
}

// ── NEW DATE CHIP COMPONENT WITH CALENDAR DIALOG ──────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageAnnouncementsDateChip(defaultText: String, icon: ImageVector) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf(defaultText) }

    val datePickerState = rememberDatePickerState()

    Box {
        // The visible Chip
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(16.dp))
                .clickable { showDatePicker = true }, // Opens the calendar
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.width(4.dp))
                // Shows either "Date" or the actual selected date
                Text(selectedDateText, fontFamily = Poppins, fontSize = 11.sp, color = Color.DarkGray)
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
            }
        }

        // The Calendar Dialog Popup
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        // Format the selected date to display on the chip
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            selectedDateText = formatter.format(Date(millis))
                        }
                    }) {
                        Text("OK", fontFamily = Poppins, color = GreenDark)
                    }
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
}

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

            // Top Section: Title and Menu
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
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.Gray
                        )
                    }

                    // Card Options Dropdown
                    DropdownMenu(
                        expanded = moreExpanded,
                        onDismissRequest = { moreExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit", fontFamily = Poppins, fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            onClick = { moreExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", fontFamily = Poppins, fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            onClick = { moreExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Archive, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            onClick = { moreExpanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Middle Section: Meta Info (Category, Date, Status)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category
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

                // Date Posted
                Text(
                    text = announcement.timePosted,
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.weight(1f))

                // Status
                if (announcement.statusText.lowercase() != "active") {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(announcement.statusColor))
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
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(announcement.statusColor))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Section: Optional Description Body
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

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ManageAnnouncementsPreview() {
    MaterialTheme {
        ManageAnnouncementsScreen()
    }
}