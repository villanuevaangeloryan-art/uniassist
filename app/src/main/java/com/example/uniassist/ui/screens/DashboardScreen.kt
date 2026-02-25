package com.example.uniassist.ui.screens

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import java.text.SimpleDateFormat
import java.util.*

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold)
)

data class Announcement(
    val title: String,
    val date: String,
    val content: String,
    val bullets: List<Pair<String, String>>,
    val footer: String
)

val announcementList = listOf(
    Announcement(
        "Campus Operations & Class Suspension",
        "January 19, 2026",
        "All on-campus classes are suspended due to bad weather.",
        listOf(
            "Online Classes:" to "Asynchronous via portal",
            "Offices:" to "Skeletal workforce",
            "Campus Access:" to "Essential staff only"
        ),
        "Stay safe everyone!"
    ),
    Announcement(
        "Scholarship Applications Open",
        "January 25, 2026",
        "Students with GPA 1.75+ may apply.",
        listOf(
            "Deadline:" to "Feb 10",
            "Where:" to "Student Portal",
            "Contact:" to "Scholarship Office"
        ),
        "Don't miss this opportunity!"
    ),
    Announcement(
        "Library Hours Extended",
        "February 1, 2026",
        "The university library will be open until 10 PM during exam week.",
        listOf(
            "New Hours:" to "8 AM – 10 PM",
            "Quiet Zones:" to "Available on 3rd floor",
            "Book Reservations:" to "Online portal"
        ),
        "Good luck with your exams!"
    )
)

@Composable
fun DashboardScreen(
    role: String = "Student",
    firstName: String = "",
    lastName: String = "",
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(), // Added to leave space for status bar
        topBar = { DashboardHeaderSection(role) },
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
            item {
                WelcomeSection(
                    userName = fullName,
                    mascotRes = mascotRes
                )
            }
            item {
                AnnouncementHeader()
            }
            items(announcementList) {
                AnnouncementCard(it)
            }
        }
    }
}

@Composable
fun DashboardHeaderSection(role: String) {
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
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = role,
                color = Color(0xFF1B5E20),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp)
            )
            Icon(Icons.Default.Notifications, null, tint = Color(0xFF1B5E20), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF1B5E20), modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun WelcomeSection(userName: String, mascotRes: Int) {
    val currentDate = Date()
    val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDate)
    val date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(currentDate)

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(195.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF377243).copy(alpha = 0.8f),
                        Color(0xFF56A566).copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .align(Alignment.CenterStart),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Welcome back,",
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                userName,
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 32.sp,
                fontSize = 30.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                day,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                date,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        Image(
            painter = painterResource(id = mascotRes),
            contentDescription = "Mascot",
            modifier = Modifier
                .size(165.dp)
                .align(Alignment.BottomEnd)
                .offset(y = 25.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun AnnouncementHeader() {
    Row(
        Modifier.padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Color(0xFFE9B44C),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                Icons.Default.Campaign,
                null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            "Announcements",
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            color = Color(0xFF1B5E20)
        )
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
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                announcement.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF1B5E20)
            )
            Text(
                "Date: ${announcement.date}",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF1B5E20).copy(alpha = 0.8f)
            )
            Text(
                announcement.content,
                fontFamily = Poppins,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF1B5E20)
            )
            announcement.bullets.forEach {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${it.first} ")
                        }
                        append(it.second)
                    },
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF1B5E20)
                )
            }
            Text(
                announcement.footer,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF1B5E20)
            )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(64.dp),
        shape = RoundedCornerShape(35.dp),
        color = Color(0xFF245B3D),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Dashboard",
                isSelected = currentRoute == "dashboard",
                onClick = { /* no action */ }
            )
            BottomNavItem(
                icon = Icons.Default.CalendarMonth,
                label = "Schedules",
                isSelected = currentRoute == "schedule",
                onClick = onScheduleClick
            )
            BottomNavItem(
                icon = Icons.Default.Info,
                label = "Campus",
                isSelected = currentRoute == "campus",
                onClick = onCampusClick
            )
            BottomNavItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                isSelected = currentRoute == "settings",
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, shape = CircleShape)
                )
            }
            Icon(
                icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF1B5E20) else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        if (!isSelected) {
            Text(
                text = label,
                fontFamily = Poppins,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}