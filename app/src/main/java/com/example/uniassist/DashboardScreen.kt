package com.example.uniassist

import androidx.compose.foundation.*
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

// ---------------------------
// DATA CLASS
// ---------------------------

data class Announcement(
    val title: String,
    val date: String,
    val content: String,
    val bullets: List<Pair<String, String>>,
    val footer: String
)

// ---------------------------
// SAMPLE DATA
// ---------------------------

val announcementList = listOf(
    Announcement(
        title = "IMPORTANT: Campus Operations & Class Suspension Update",
        date = "January 19, 2026",
        content = "Please be advised that all on-campus classes and office operations at University of Cabuyao are suspended tomorrow, January 20, due to the forecasted inclement weather.",
        bullets = listOf(
            "Online Classes:" to "Asynchronous learning will be conducted via the student portal.",
            "Offices:" to "Administrative offices will operate on a skeletal workforce.",
            "Campus Access:" to "Only essential personnel will be allowed entry."
        ),
        footer = "Regular operations are expected to resume on Wednesday, January 21. Stay safe, everyone!"
    ),

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

// ---------------------------
// DASHBOARD SCREEN
// ---------------------------

@Composable
fun DashboardScreen() {
    Scaffold(
        bottomBar = { FloatingBottomNavigationBar() }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))



            Spacer(modifier = Modifier.height(24.dp))

            AnnouncementHeader()

            Spacer(modifier = Modifier.height(12.dp))

            AnnouncementsList()
        }
    }
}

// ---------------------------
// HEADER
// ---------------------------

@Composable
fun HeaderSection() {
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
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(Icons.Default.Notifications, null, tint = Color(0xFF1B5E20))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF1B5E20))
        }
    }
}

// ---------------------------
// WELCOME
// ---------------------------

@Composable
fun WelcomeSection() {
    Column(Modifier.padding(horizontal = 24.dp)) {
        Text("Welcome back,", fontSize = 18.sp, color = DarkGreen)
        Text(
            "Angelo Villanueva",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )
    }
}

// ---------------------------
// ANNOUNCEMENT HEADER
// ---------------------------

@Composable
fun AnnouncementHeader() {

    WelcomeSection()

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

// ---------------------------
// ANNOUNCEMENT LIST
// ---------------------------

@Composable
fun AnnouncementsList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(announcementList) {
            AnnouncementCard(it)
        }
    }
}

// ---------------------------
// ANNOUNCEMENT CARD
// ---------------------------

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

// ---------------------------
// BULLET ITEM
// ---------------------------

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

// ---------------------------
// BOTTOM NAV
// ---------------------------

@Composable
fun FloatingBottomNavigationBar() {
    Surface(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(58.dp),
        shape = RoundedCornerShape(35.dp),
        color = Color(0xFF245B3D)
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(Icons.Default.Home, "Dashboard")
            BottomNavItem(Icons.Default.DateRange, "Schedules")
            BottomNavItem(Icons.Default.Info, "Campus")
            BottomNavItem(Icons.Default.Settings, "Settings")
        }
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = Color.White)
        Text(label, fontSize = 10.sp, color = Color.White)
    }
}

// ---------------------------
// PREVIEW
// ---------------------------

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    DashboardScreen()
}
