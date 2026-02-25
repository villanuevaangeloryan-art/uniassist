package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import java.text.SimpleDateFormat
import java.util.*

// Import Poppins from the existing DashboardScreen (same package)
import com.example.uniassist.ui.screens.Poppins

// ── Colors ────────────────────────────────────────────────────────────────────
private val GreenDark       = Color(0xFF1B5E20)
private val GreenMedium     = Color(0xFF56A566)
private val GreenLight      = Color(0xFFB7E4C7)

// ── Teacher Dashboard ─────────────────────────────────────────────────────────
@Composable
fun TeacherDashboardScreen(
    role: String = "Teacher",
    userName: String = "Prof. Angelo Villanueva",
    totalStudents: Int = 0,
    totalAnnouncements: Int = 0,
    totalUnread: Int = 0,
    onManageAnnouncements: () -> Unit = {},
    onManageCampusInfo: () -> Unit = {},
    onViewStudents: () -> Unit = {},
    onSettings: () -> Unit = {},
    onDrafts: () -> Unit = {},
    onArchive: () -> Unit = {},
    onLogout: () -> Unit = {}   // ← new logout callback
) {
    val currentDate = remember { Date() }
    val dayLabel  = remember { SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDate) }
    val dateLabel = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(currentDate) }

    val mascotRes = R.drawable.uniassist_teacher

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Nav Bar
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
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = GreenDark,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings", fontFamily = Poppins, fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(18.dp))
                                },
                                onClick = {
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Drafts", fontFamily = Poppins, fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Description, null, modifier = Modifier.size(18.dp))
                                },
                                onClick = {
                                    menuExpanded = false
                                    onDrafts()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Archive", fontFamily = Poppins, fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Archive, null, modifier = Modifier.size(18.dp))
                                },
                                onClick = {
                                    menuExpanded = false
                                    onArchive()
                                }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Logout", fontFamily = Poppins, fontSize = 14.sp, color = Color(0xFFD32F2F)) },
                                leadingIcon = {
                                    Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                                },
                                onClick = {
                                    menuExpanded = false
                                    onLogout()
                                }
                            )
                        }
                    }
                }
            }

            // Welcome Banner (unchanged)
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
                        )
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
                        fontSize = 28.sp,
                        color = Color.White,
                        lineHeight = 32.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        dayLabel,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        dateLabel,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Image(
                    painter = painterResource(id = mascotRes),
                    contentDescription = "Mascot",
                    modifier = Modifier
                        .size(165.dp)
                        .align(Alignment.BottomEnd)
                        .offset(y = 25.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Actions Header
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GreenMedium,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "Actions",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = GreenDark
                )
            }

            Spacer(Modifier.height(12.dp))

            ActionItem(
                icon = Icons.Default.Campaign,
                label = "Manage Announcements",
                backgroundRes = R.drawable.background_rolepick,
                onClick = onManageAnnouncements
            )
            Spacer(Modifier.height(10.dp))
            ActionItem(
                icon = Icons.Default.Business,
                label = "Manage Campus Information",
                backgroundRes = R.drawable.background_rolepick,
                onClick = onManageCampusInfo
            )
            Spacer(Modifier.height(10.dp))
            ActionItem(
                icon = Icons.Default.People,
                label = "View Students",
                backgroundRes = R.drawable.background_rolepick,
                onClick = onViewStudents
            )

            Spacer(Modifier.height(20.dp))

            // Totals Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 8.dp)
            ) {
                // Shadow card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, top = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2E7D4F))
                        .height(IntrinsicSize.Min)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        StatRow("Total Students",             totalStudents,    ghost = true)
                        HorizontalDivider(color = Color.Transparent, thickness = 1.dp)
                        StatRow("Total Announcements",        totalAnnouncements, ghost = true)
                        HorizontalDivider(color = Color.Transparent, thickness = 1.dp)
                        StatRow("Total Unread Announcements", totalUnread,       ghost = true)
                    }
                }

                // Front card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFD5FFDE))
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1B5E20),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        StatRow("Total Students", totalStudents)
                        HorizontalDivider(color = Color(0xFF78C48A), thickness = 1.dp)
                        StatRow("Total Announcements", totalAnnouncements)
                        HorizontalDivider(color = Color(0xFF78C48A), thickness = 1.dp)
                        StatRow("Total Unread Announcements", totalUnread)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

// ── Action Item ───────────────────────────────────────────────────────────────
@Composable
private fun ActionItem(
    icon: ImageVector,
    label: String,
    backgroundRes: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0XFF56A566), RoundedCornerShape(14.dp))
            .height(64.dp)
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.80f
        )

        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(GreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GreenDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    text = label,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color(0xFF2B4B49),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = GreenDark,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// ── Stat Row ──────────────────────────────────────────────────────────────────
@Composable
private fun StatRow(label: String, value: Int, ghost: Boolean = false) {
    val textAlpha = if (ghost) 0f else 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF245B3D).copy(alpha = textAlpha)
        )
        Text(
            text = value.toString(),
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 34.sp,
            color = Color(0xFF1B5E20).copy(alpha = textAlpha)
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeacherDashboardPreview() {
    MaterialTheme {
        TeacherDashboardScreen(
            userName = "Prof. Jane Doe",
            totalStudents = 0,
            totalAnnouncements = 0,
            totalUnread = 0
        )
    }
}