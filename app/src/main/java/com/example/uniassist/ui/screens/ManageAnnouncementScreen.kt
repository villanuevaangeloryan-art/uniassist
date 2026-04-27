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
    val isArchived: Boolean = false
)

fun NewAnnouncement.toManageCard(id: Int): ManageScreenAnnouncement {
    val (icon, color) = categoryIconAndColor(category)
    return ManageScreenAnnouncement(
        id            = id,
        title         = title,
        category      = category,
        categoryIcon  = icon,
        categoryColor = color,
        timePosted    = "just now",
        statusText    = "Active",
        statusColor   = StatusActive,
        description   = message.ifBlank { null }
    )
}

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
        isArchived    = isArchived
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

    // ── Filter / Sort state ────────────────────────────────────────────────────
    var selectedSort   by remember { mutableStateOf("Newest") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedDate   by remember { mutableStateOf<String?>(null) }

    var announcementToDelete by remember { mutableStateOf<ManageScreenAnnouncement?>(null) }

    // ── Apply search → filter → sort → date ───────────────────────────────────
    val displayed = remember(dbAnnouncements, searchQuery, selectedSort, selectedFilter, selectedDate) {

        // 1. Search
        var result = if (searchQuery.isBlank()) dbAnnouncements
        else dbAnnouncements.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true)
        }

        // 2. Filter by status
        result = when (selectedFilter) {
            "Active"       -> result.filter { !it.isArchived && it.statusText == "Active" }
            "Archived"     -> result.filter { it.isArchived }
            "Ending soon"  -> result.filter { it.statusText == "Ending soon" }
            "Expired"      -> result.filter { it.statusText == "Expired" }
            else           -> result // "All"
        }

        // 3. Filter by date
        if (!selectedDate.isNullOrBlank()) {
            result = result.filter { it.timePosted == selectedDate }
        }

        // 4. Sort
        result = when (selectedSort) {
            "Oldest"   -> result.sortedBy { it.id }
            "Archived" -> result.sortedByDescending { it.isArchived }
            else       -> result.sortedByDescending { it.id } // "Newest"
        }

        result
    }

    // ── Delete dialog ─────────────────────────────────────────────────────────
    announcementToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { announcementToDelete = null },
            containerColor   = Color.White,
            icon = {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = null,
                    tint     = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Delete Announcement?",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    textAlign  = TextAlign.Center
                )
            },
            text = {
                Text(
                    "\"${target.title}\" will be permanently removed. This cannot be undone.",
                    fontFamily = Poppins,
                    fontSize   = 13.sp,
                    color      = Color.DarkGray,
                    textAlign  = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(target)
                        announcementToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape  = RoundedCornerShape(8.dp)
                ) {
                    Text("Delete", fontFamily = Poppins, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { announcementToDelete = null },
                    shape   = RoundedCornerShape(8.dp),
                    border  = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text("Cancel", fontFamily = Poppins, color = Color.DarkGray)
                }
            }
        )
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
                modifier       = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("New Announcement", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            // ── Top Bar ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
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
                        DropdownMenu(
                            expanded         = profileMenuExpanded,
                            onDismissRequest = { profileMenuExpanded = false },
                            modifier         = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text        = { Text("Settings", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(16.dp)) },
                                onClick     = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text        = { Text("Drafts", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Description, null, modifier = Modifier.size(16.dp)) },
                                onClick     = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text        = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Archive, null, modifier = Modifier.size(16.dp)) },
                                onClick     = { profileMenuExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Search Bar ─────────────────────────────────────────────────────
            OutlinedTextField(
                value         = searchQuery,
                onValueChange = { searchQuery = it },
                modifier      = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(52.dp),
                placeholder   = { Text("Search announcements . . .", fontFamily = Poppins, fontSize = 14.sp, color = Color.Gray) },
                leadingIcon   = {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, end = 8.dp)) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search", tint = GreenDark)
                        Spacer(modifier = Modifier.width(12.dp))
                        VerticalDivider(modifier = Modifier.height(24.dp), color = GreenMedium.copy(alpha = 0.5f), thickness = 1.dp)
                    }
                },
                shape      = RoundedCornerShape(12.dp),
                colors     = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = GreenMedium,
                    unfocusedBorderColor    = GreenMedium.copy(alpha = 0.6f),
                    focusedContainerColor   = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Filter Chips — now fully controlled ────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Sort chip
                SortChip(
                    selected  = selectedSort,
                    onSelect  = { selectedSort = it }
                )
                // Filter chip
                FilterChip(
                    selected  = selectedFilter,
                    onSelect  = { selectedFilter = it }
                )
                // Date chip — shows clear X when a date is active
                DateChip(
                    selectedDate = selectedDate,
                    onDateSelect = { selectedDate = it },
                    onClear      = { selectedDate = null }
                )
            }

            // ── Active filter summary row ──────────────────────────────────────
            val hasActiveFilters = selectedFilter != "All" || selectedDate != null || selectedSort != "Newest"
            if (hasActiveFilters) {
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "${displayed.size} result${if (displayed.size != 1) "s" else ""}",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Color.Gray
                    )
                    TextButton(
                        onClick = {
                            selectedSort   = "Newest"
                            selectedFilter = "All"
                            selectedDate   = null
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp), tint = GreenDark)
                        Spacer(Modifier.width(4.dp))
                        Text("Clear filters", fontFamily = Poppins, fontSize = 11.sp, color = GreenDark)
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Announcements List ─────────────────────────────────────────────
            if (displayed.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = when {
                            hasActiveFilters -> "No announcements match the current filters."
                            searchQuery.isNotBlank() -> "No results for \"$searchQuery\"."
                            else -> "No announcements yet.\nTap + New Announcement to create one."
                        },
                        fontFamily = Poppins,
                        fontSize   = 14.sp,
                        color      = Color.Gray,
                        textAlign  = TextAlign.Center
                    )
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
                            onEditRequest   = { onEditNavigate(it.id) }
                        )
                    }
                }
            }
        }
    }
}

// ── Sort Chip ─────────────────────────────────────────────────────────────────
@Composable
private fun SortChip(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options  = listOf("Newest", "Oldest", "Archived first")
    val isActive = selected != "Newest"

    Box {
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isActive) GreenMedium.copy(alpha = 0.15f) else Color.White)
                .border(BorderStroke(1.dp, if (isActive) GreenMedium else Color.LightGray), RoundedCornerShape(16.dp))
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier              = Modifier.padding(horizontal = 12.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.AutoMirrored.Outlined.Sort, null, tint = GreenDark, modifier = Modifier.size(14.dp))
                Text(
                    text       = if (isActive) selected else "Sort by",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = GreenDark,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                )
                Icon(Icons.Default.ArrowDropDown, null, tint = GreenDark, modifier = Modifier.size(14.dp))
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (selected == option) {
                                Icon(Icons.Default.Check, null, tint = GreenDark, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(6.dp))
                            }
                            Text(option, fontFamily = Poppins, fontSize = 12.sp,
                                fontWeight = if (selected == option) FontWeight.SemiBold else FontWeight.Normal)
                        }
                    },
                    onClick = { onSelect(option); expanded = false }
                )
            }
        }
    }
}

// ── Filter by Status Chip ─────────────────────────────────────────────────────
@Composable
private fun FilterChip(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options  = listOf("All", "Active", "Archived", "Ending soon", "Expired")
    val isActive = selected != "All"

    Box {
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isActive) GreenMedium.copy(alpha = 0.15f) else Color.White)
                .border(BorderStroke(1.dp, if (isActive) GreenMedium else Color.LightGray), RoundedCornerShape(16.dp))
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier              = Modifier.padding(horizontal = 12.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Outlined.FilterList, null, tint = GreenDark, modifier = Modifier.size(14.dp))
                Text(
                    text       = if (isActive) selected else "Filter by",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = GreenDark,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                )
                Icon(Icons.Default.ArrowDropDown, null, tint = GreenDark, modifier = Modifier.size(14.dp))
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (selected == option) {
                                Icon(Icons.Default.Check, null, tint = GreenDark, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(6.dp))
                            }
                            Text(option, fontFamily = Poppins, fontSize = 12.sp,
                                fontWeight = if (selected == option) FontWeight.SemiBold else FontWeight.Normal)
                        }
                    },
                    onClick = { onSelect(option); expanded = false }
                )
            }
        }
    }
}

// ── Date Chip ─────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateChip(
    selectedDate : String?,
    onDateSelect : (String) -> Unit,
    onClear      : () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val isActive = !selectedDate.isNullOrBlank()

    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isActive) GreenMedium.copy(alpha = 0.15f) else Color.White)
            .border(BorderStroke(1.dp, if (isActive) GreenMedium else Color.LightGray), RoundedCornerShape(16.dp))
            .clickable { showDatePicker = true },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier              = Modifier.padding(horizontal = 12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(Icons.Outlined.CalendarToday, null, tint = GreenDark, modifier = Modifier.size(14.dp))
            Text(
                text       = selectedDate ?: "Date",
                fontFamily = Poppins,
                fontSize   = 12.sp,
                color      = GreenDark,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
            )
            // Show X to clear date filter when active
            if (isActive) {
                Spacer(Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(GreenMedium)
                        .clickable { onClear() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(9.dp))
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton    = {
                TextButton(onClick = {
                    showDatePicker = false
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                        onDateSelect(formatter.format(Date(millis)))
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
                state  = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = GreenMedium,
                    todayDateBorderColor      = GreenDark,
                    todayContentColor         = GreenDark
                )
            )
        }
    }
}

// ── Card composable ───────────────────────────────────────────────────────────
@Composable
private fun ManageAnnouncementCard(
    announcement    : ManageScreenAnnouncement,
    onArchive       : (Int) -> Unit = {},
    onUnarchive     : (Int) -> Unit = {},
    onDeleteRequest : (ManageScreenAnnouncement) -> Unit = {},
    onEditRequest   : (ManageScreenAnnouncement) -> Unit = {}
) {
    var moreExpanded by remember { mutableStateOf(false) }

    Card(
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = if (announcement.isArchived) Color(0xFFF0F0F0) else Color.White),
        border    = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
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
                        color      = if (announcement.isArchived) Color.Gray else GreenDark,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.weight(1f, fill = false)
                    )
                    if (announcement.isArchived) {
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF9E9E9E)) {
                            Text("Archived", fontFamily = Poppins, fontSize = 10.sp, color = Color.White,
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
                            onClick     = { moreExpanded = false; onEditRequest(announcement) }
                        )
                        DropdownMenuItem(
                            text        = { Text("Delete", fontFamily = Poppins, fontSize = 12.sp, color = Color(0xFFD32F2F)) },
                            leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(16.dp)) },
                            onClick     = { moreExpanded = false; onDeleteRequest(announcement) }
                        )
                        if (announcement.isArchived) {
                            DropdownMenuItem(
                                text        = { Text("Unarchive", fontFamily = Poppins, fontSize = 12.sp, color = GreenDark) },
                                leadingIcon = { Icon(Icons.Outlined.Unarchive, null, tint = GreenDark, modifier = Modifier.size(16.dp)) },
                                onClick     = { moreExpanded = false; onUnarchive(announcement.id) }
                            )
                        } else {
                            DropdownMenuItem(
                                text        = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Archive, null, modifier = Modifier.size(16.dp)) },
                                onClick     = { moreExpanded = false; onArchive(announcement.id) }
                            )
                        }
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
                Spacer(Modifier.weight(1f))
                if (announcement.statusText.lowercase() != "active") {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(announcement.statusColor))
                    Spacer(Modifier.width(4.dp))
                }
                Text(announcement.statusText, fontFamily = Poppins, fontSize = 11.sp, color = Color.DarkGray)
                if (announcement.statusText.lowercase() == "active") {
                    Spacer(Modifier.width(4.dp))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(announcement.statusColor))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (announcement.description != null) {
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 1.dp)
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF7F9F8)).padding(16.dp)) {
                    Text(announcement.description, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.Black)
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ManageAnnouncementsPreview() {
    MaterialTheme { ManageAnnouncementsScreen() }
}