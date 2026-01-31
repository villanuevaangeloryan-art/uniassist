package com.example.uniassist

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
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

// Define the brand colors based on the image design
val DarkGreen = Color(0xFF1B4332)
val LightGreenBorder = Color(0xFF99C24D)
val HeaderGradientStart = Color(0xFF2D6A4F)
val HeaderGradientEnd = Color(0xFFD8E2DC)

@Composable
fun DashboardScreen() {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
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
            .height(70.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(HeaderGradientStart.copy(alpha = 0.7f), HeaderGradientEnd),
                )
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp) // Offset for status bar area
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = DarkGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                modifier = Modifier.size(40.dp),
                border = BorderStroke(1.5.dp, DarkGreen)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = DarkGreen,
                    modifier = Modifier.padding(2.dp).fillMaxSize()
                )
            }
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
fun BottomNavigationBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 20.dp, end = 20.dp) // Creates the floating effect
    ) {
        Surface(
            color = DarkGreen,
            shape = CircleShape, // Capsule shape
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(Icons.Default.Home, "Dashboard", true)
                BottomNavItem(Icons.Default.EventAvailable, "Schedules", false)
                BottomNavItem(Icons.Default.Info, "Campus", false)
                BottomNavItem(Icons.Default.Settings, "Settings", false)
            }
        }
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}
