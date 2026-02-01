package com.example.uniassist

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.ui.theme.DarkGreen
import com.example.uniassist.ui.theme.LightGreenBorder
import androidx.compose.material3.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ---------------- DATA CLASS ----------------

data class Announcement(
    val title: String,
    val date: String,
    val content: String,
    val bullets: List<Pair<String, String>>,
    val footer: String
)

// ---------------- SAMPLE DATA ----------------

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
        "Intramurals 2026",
        "February 2, 2026",
        "University intramurals start next month.",
        listOf(
            "Registration:" to "PE Office",
            "Tryouts:" to "Feb 10–15",
            "Events:" to "Basketball, Volleyball"
        ),
        "Represent your department!"
    )
)

// ---------------- DASHBOARD ----------------

@Composable
fun DashboardScreen() {

    Scaffold(
        topBar = { HeaderSection() }, // ✅ Fixed header
        bottomBar = { FloatingBottomNavigationBar() }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item { WelcomeSection() }

            item { AnnouncementHeader() }

            items(announcementList) {
                AnnouncementCard(it)
            }
        }
    }
}

// ---------------- FIXED HEADER ----------------

@Composable
fun HeaderSection() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF7CA786),
                        Color(0xFFE2F1C6)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ---------------- WELCOME ----------------

@Composable
fun WelcomeSection() {

    val currentDate = Date()

    val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    val day = dayFormatter.format(currentDate)
    val date = dateFormatter.format(currentDate)

    Column(
        Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
    ) {

        Text(
            text = "Welcome back,",
            fontSize = 18.sp,
            color = DarkGreen
        )

        Text(
            text = "Angelo Villanueva",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Day + Date in GREEN
        Text(
            text = "$day, $date",
            fontSize = 14.sp,
            color = DarkGreen
        )
    }
}


// ---------------- ANNOUNCEMENT HEADER ----------------

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
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )
    }
}

// ---------------- ANNOUNCEMENT CARD ----------------

@Composable
fun AnnouncementCard(announcement: Announcement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(1.5.dp, LightGreenBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(Color(0xFFF8FAF8))
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                announcement.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkGreen
            )

            Text(
                "Date: ${announcement.date}",
                fontWeight = FontWeight.Medium,
                color = DarkGreen
            )

            Text(announcement.content, color = DarkGreen)

            announcement.bullets.forEach {
                BulletItem(it.first, it.second)
            }

            Text(
                announcement.footer,
                fontWeight = FontWeight.Medium,
                color = DarkGreen
            )
        }
    }
}

// ---------------- BULLET ITEM ----------------

@Composable
fun BulletItem(title: String, description: String) {
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$title ")
            }
            append(description)
        },
        color = DarkGreen,
        fontSize = 14.sp
    )
}

// ---------------- BOTTOM NAV ----------------

@Composable
fun FloatingBottomNavigationBar() {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .height(58.dp),
        shape = RoundedCornerShape(35.dp),
        color = Color(0xFF245B3D),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(icon = Icons.Default.Home, label = "Dashboard")
            BottomNavItem(icon = Icons.Default.DateRange, label = "Schedules")
            BottomNavItem(icon = Icons.Default.Info, label = "Campus")
            BottomNavItem(icon = Icons.Default.Settings, label = "Settings")
        }
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, label: String, isSelected: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF1B5E20) else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Label Text
        if (!isSelected) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        } else {
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
// ---------------- PREVIEW ----------------

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    DashboardScreen()
}
