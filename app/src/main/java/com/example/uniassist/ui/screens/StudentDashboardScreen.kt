package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import com.example.uniassist.ui.viewmodel.AnnouncementViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold)
)

@Composable
fun DashboardScreen(
    role: String = "Student",
    firstName: String = "",
    lastName: String = "",
    announcementViewModel: AnnouncementViewModel,
    onScheduleClick: () -> Unit = {},
    onCampusClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val mascotRes = when (role) {
        "Teacher" -> R.drawable.uniassist_teacher
        else -> R.drawable.uniassist_student
    }
    val fullName = if (firstName.isNotBlank() && lastName.isNotBlank()) {
        "$firstName $lastName"
    } else {
        if (role == "Teacher") "Prof. Jane Doe" else "Jane Doe"
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val announcements by announcementViewModel.activeAnnouncements.collectAsState()
    val unreadCount by announcementViewModel.unreadCount.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            DashboardHeaderSection(
                role = role,
                unreadCount = unreadCount,
                notifications = announcements,
                onMarkAllAsRead = { announcementViewModel.markAllAsRead() },
                onViewAnnouncements = {
                    scope.launch { listState.animateScrollToItem(1) }
                },
                onNotificationClicked = { announcement ->
                    announcementViewModel.markAsRead(announcement.id)
                }
            )
        },
        bottomBar = {
            DashboardBottomNavigationBar(
                currentRoute = "dashboard",
                onScheduleClick = onScheduleClick,
                onCampusClick = onCampusClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item { WelcomeSection(userName = fullName, mascotRes = mascotRes) }
            item { AnnouncementHeader() }
            items(announcements) { announcement ->
                AnnouncementCard(
                    announcement = announcement,
                    onReadClick = { announcementViewModel.markAsRead(announcement.id) }
                )
            }
        }
    }
}

@Composable
fun AnnouncementCard(
    announcement: Announcement,
    onReadClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(
                width = 1.dp,
                color = if (!announcement.isRead) Color(0xFF4CAF50).copy(alpha = 0.5f) else Color(0xFFDDE9E1),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (!announcement.isRead) Color(0xFFF1F8F1) else Color(0xFFF8FAF8)
        )
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.weight(1f)
                )
                if (!announcement.isRead) {
                    Surface(
                        color = Color(0xFFE9B44C),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "NEW",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Text(
                text = "Date: ${announcement.date}",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF1B5E20).copy(alpha = 0.8f)
            )

            Text(
                text = announcement.content,
                fontFamily = Poppins,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF1B5E20)
            )

            if (announcement.bullets.isNotEmpty()) {
                announcement.bullets.forEach {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("${it.first} ") }
                            append(it.second)
                        },
                        fontFamily = Poppins, fontSize = 14.sp, lineHeight = 20.sp, color = Color(0xFF1B5E20)
                    )
                }
            }

            if (announcement.footer.isNotBlank()) {
                Text(announcement.footer, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1B5E20))
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = onReadClick,
                    enabled = !announcement.isRead,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1B5E20),
                        disabledContainerColor = Color(0xFFDDE9E1)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = if (announcement.isRead) Icons.Default.DoneAll else Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (announcement.isRead) Color(0xFF1B5E20) else Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (announcement.isRead) "Read" else "Read",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (announcement.isRead) Color(0xFF1B5E20) else Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardHeaderSection(
    role: String,
    unreadCount: Int,
    notifications: List<Announcement>,
    onMarkAllAsRead: () -> Unit,
    onViewAnnouncements: () -> Unit,
    onNotificationClicked: (Announcement) -> Unit
) {
    var notificationsExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(Brush.horizontalGradient(listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(role, color = Color(0xFF1B5E20), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(containerColor = Color(0xFFE9B44C)) { Text(unreadCount.coerceAtMost(99).toString()) }
                        }
                    }
                ) {
                    IconButton(onClick = { notificationsExpanded = true }) {
                        Icon(
                            Icons.Default.Notifications,
                            null,
                            tint = Color(0xFF1B5E20),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                NotificationsDropdownMenu(
                    role = role,
                    unreadCount = unreadCount,
                    notifications = notifications,
                    expanded = notificationsExpanded,
                    onDismiss = { notificationsExpanded = false },
                    onMarkAllAsRead = onMarkAllAsRead,
                    onViewAnnouncements = onViewAnnouncements,
                    onNotificationClicked = onNotificationClicked
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF1B5E20), modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
private fun NotificationsDropdownMenu(
    role: String,
    unreadCount: Int,
    notifications: List<Announcement>,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onMarkAllAsRead: () -> Unit,
    onViewAnnouncements: () -> Unit,
    onNotificationClicked: (Announcement) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        if (notifications.isEmpty()) {
            DropdownMenuItem(
                text = { Text("No notifications yet") },
                onClick = onDismiss
            )
            return@DropdownMenu
        }

        if (unreadCount > 0) {
            DropdownMenuItem(
                text = { Text("Mark all as read", fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20)) },
                onClick = {
                    onMarkAllAsRead()
                    onDismiss()
                }
            )
            HorizontalDivider()
        }

        notifications.take(5).forEach { announcement ->
            DropdownMenuItem(
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!announcement.isRead) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(percent = 50))
                                        .background(Color(0xFFE9B44C))
                                )
                            }
                            Text(
                                announcement.title,
                                fontWeight = if (announcement.isRead) FontWeight.Normal else FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            announcement.date,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                },
                onClick = {
                    onNotificationClicked(announcement)
                    onDismiss()
                }
            )
        }

        HorizontalDivider()
        DropdownMenuItem(
            text = { Text("View all announcements", fontSize = 13.sp, color = Color(0xFF1B5E20)) },
            onClick = {
                onViewAnnouncements()
                onDismiss()
            }
        )
    }
}

@Composable
fun WelcomeSection(userName: String, mascotRes: Int) {
    val currentDate = rememberCurrentDateAutoUpdate()
    val day = remember(currentDate) { SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDate) }
    val date = remember(currentDate) { SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(currentDate) }

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF377243), Color(0xFF56A566))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(end = 120.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome back,", fontFamily = Poppins, fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
            Text(
                userName,
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(day,  fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            Text(date, fontFamily = Poppins, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
        }
        Image(
            painter = painterResource(id = mascotRes),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 10.dp, y = 10.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun rememberCurrentDateAutoUpdate(): Date {
    var now by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            now = Date()
        }
    }
    return now
}

@Composable
fun AnnouncementHeader() {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Color(0xFFE9B44C),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(Icons.Default.Campaign, null, tint = Color.White, modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            "Announcements",
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            color = Color(0xFF1B5E20)
        )
    }
}

@Composable
fun DashboardBottomNavigationBar(
    currentRoute: String,
    onScheduleClick: () -> Unit,
    onCampusClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(64.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color(0xFF1B5E20),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(Icons.Default.Home, "Home", currentRoute == "dashboard", {})
            BottomNavItem(Icons.Default.CalendarMonth, "Schedules", currentRoute == "schedule", onScheduleClick)
            BottomNavItem(Icons.Default.Info, "Campus", currentRoute == "campus", onCampusClick)
            BottomNavItem(Icons.Default.Settings, "Settings", currentRoute == "settings", onSettingsClick)
        }
    }
}
