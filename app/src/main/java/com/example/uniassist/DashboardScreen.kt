package com.example.uniassist

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
                .verticalScroll(rememberScrollState())
        ) {
            // Custom Gradient Header
            HeaderSection()

            Spacer(modifier = Modifier.height(32.dp))

            // Welcome Section
            WelcomeSection()

            Spacer(modifier = Modifier.height(40.dp))

            // Announcements Title with Icon
            AnnouncementHeader()

            // Announcements Content Card
            AnnouncementsCard()

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

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

@Composable
fun WelcomeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Welcome back,",
            fontSize = 18.sp,
            color = DarkGreen,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Angelo Villanueva",
            fontSize = 32.sp,
            color = DarkGreen,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AnnouncementHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Color(0xFFE9B44C),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.size(44.dp),
            shadowElevation = 4.dp
        ) {
            Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Announcements",
            fontSize = 26.sp,
            color = DarkGreen,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun AnnouncementsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(1.5.dp, LightGreenBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF8)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("IMPORTANT: ")
                    }
                    append("Campus Operations & Class Suspension Update")
                },
                color = DarkGreen,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Date: ")
                    }
                    append("January 19, 2026")
                },
                color = DarkGreen,
                fontSize = 15.sp
            )

            Text(
                text = "Please be advised that all on-campus classes and office operations at University of Cabuyao are suspended tomorrow, January 20, due to the forecasted inclement weather.",
                color = DarkGreen,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            BulletItem("Online Classes:", "Asynchronous learning will be conducted via the student portal.")
            BulletItem("Offices:", "Administrative offices will operate on a skeletal workforce.")
            BulletItem("Campus Access:", "Only essential personnel will be allowed entry.")

            Text(
                text = "Regular operations are expected to resume on Wednesday, January 21. Stay safe, everyone!",
                color = DarkGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun BulletItem(title: String, description: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("   $title ")
            }
            append(description)
        },
        color = DarkGreen,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
}

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

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}
