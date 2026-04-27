package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import com.example.uniassist.ui.viewmodel.AnnouncementViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
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
    // MVVM
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

    // Collect only non-archived announcements for the student view.
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item { WelcomeSection(userName = fullName, mascotRes = mascotRes) }
            item { AnnouncementHeader() }
            // Driven by StateFlow
            items(announcements) { announcement ->
                AnnouncementCard(announcement)
            }
        }
    }
}

@Composable
fun DashboardHeaderSection(
    role: String,
    unreadCount: Int,
    notifications: List<Announcement>,
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
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(role, color = Color(0xFF1B5E20), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp))
            BadgedBox(
                badge = {
                    if (role == "Student" && unreadCount > 0) {
                        Badge { Text(unreadCount.coerceAtMost(99).toString()) }
                    }
                }
            ) {
                IconButton(onClick = { notificationsExpanded = true }) {
                    Icon(
                        Icons.Default.Notifications,
                        null,
                        tint = Color(0xFF1B5E20),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            DropdownMenu(
                expanded = notificationsExpanded,
                onDismissRequest = { notificationsExpanded = false }
            ) {
                if (role != "Student") {
                    DropdownMenuItem(
                        text = { Text("Notifications are available for students") },
                        onClick = { notificationsExpanded = false }
                    )
                } else if (notifications.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No notifications yet") },
                        onClick = { notificationsExpanded = false }
                    )
                } else {
                    notifications.take(5).forEach { announcement ->
                        DropdownMenuItem(
                            text = {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        announcement.title,
                                        fontWeight = if (announcement.isRead) FontWeight.Normal else FontWeight.Bold
                                    )
                                    Text(
                                        announcement.date,
                                        fontSize = 12.sp,
                                        color = Color(0xFF1B5E20).copy(alpha = 0.75f)
                                    )
                                }
                            },
                            onClick = {
                                onNotificationClicked(announcement)
                                notificationsExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF1B5E20), modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun WelcomeSection(userName: String, mascotRes: Int) {
    val currentDate = rememberCurrentDateAutoUpdate()
    val day = remember(currentDate) {
        SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDate)
    }
    val date = remember(currentDate) {
        SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(currentDate)
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(195.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF377243).copy(alpha = 0.8f), Color(0xFF56A566).copy(alpha = 0.8f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(end = 140.dp)
                .align(Alignment.CenterStart),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome back,", fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color.White)
            Text(userName, fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, lineHeight = 32.sp, fontSize = 30.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(day,  fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.White)
            Text(date, fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
        }
        Image(
            painter = painterResource(id = mascotRes),
            contentDescription = "Mascot",
            modifier = Modifier
                .size(165.dp)
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun rememberCurrentDateAutoUpdate(): Date {
    var now by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            val cal = Calendar.getInstance()
            cal.time = now
            cal.add(Calendar.DAY_OF_YEAR, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            val delayMs = (cal.timeInMillis - System.currentTimeMillis()).coerceAtLeast(1_000)
            delay(delayMs)
            now = Date()
        }
    }

    return now
}

@Composable
fun AnnouncementHeader() {
    Row(Modifier.padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(color = Color(0xFFE9B44C), shape = RoundedCornerShape(10.dp), modifier = Modifier.size(44.dp)) {
            Icon(Icons.Default.Campaign, null, tint = Color.White, modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text("Announcements", fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Color(0xFF1B5E20))
    }
}

@Composable
fun AnnouncementCard(announcement: Announcement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(1.dp, Color(0xFFDDE9E1), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(Color(0xFFF8FAF8))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(announcement.title, fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF1B5E20))
            Text("Date: ${announcement.date}", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1B5E20).copy(alpha = 0.8f))
            Text(announcement.content, fontFamily = Poppins, fontSize = 14.sp, lineHeight = 20.sp, color = Color(0xFF1B5E20))
            announcement.bullets.forEach {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("${it.first} ") }
                        append(it.second)
                    },
                    fontFamily = Poppins, fontSize = 14.sp, lineHeight = 20.sp, color = Color(0xFF1B5E20)
                )
            }
            if (announcement.footer.isNotBlank()) {
                Text(announcement.footer, fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1B5E20))
            }
        }
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
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(64.dp),
        shape = RoundedCornerShape(35.dp),
        color = Color(0xFF245B3D),
        shadowElevation = 8.dp
    ) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            BottomNavItem(Icons.Default.Home,          "Dashboard", currentRoute == "dashboard", {})
            BottomNavItem(Icons.Default.CalendarMonth, "Schedules", currentRoute == "schedule",  onScheduleClick)
            BottomNavItem(Icons.Default.Info,          "Campus",    currentRoute == "campus",    onCampusClick)
            BottomNavItem(Icons.Default.Settings,      "Settings",  currentRoute == "settings",  onSettingsClick)
        }
    }
}