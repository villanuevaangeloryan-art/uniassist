package com.example.uniassist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import com.example.uniassist.ui.viewmodel.AnnouncementViewModel

private val GreenDark       = Color(0xFF1B5E20)
private val GreenMedium     = Color(0xFF56A566)
private val GreenLight      = Color(0xFFB7E4C7)

@Composable
fun TeacherDashboardScreen(
    role: String = "Teacher",
    userName: String = "Prof. Angelo Villanueva",
    totalStudents: Int = 0,
    totalAnnouncements: Int = 0,
    totalUnread: Int = 0,
    currentRoute: String = "dashboard",
    announcementViewModel: AnnouncementViewModel,
    onDashboardClick: () -> Unit = {},
    onDraftsClick: () -> Unit = {},
    onArchiveClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onManageAnnouncements: () -> Unit = {},
    onManageCampusInfo: () -> Unit = {},
    onViewStudents: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val mascotRes = R.drawable.uniassist_teacher
    var menuExpanded by remember { mutableStateOf(false) }
    
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TeacherDashboardTopBar(
                role = role,
                onLogout = onLogout,
                menuExpanded = menuExpanded,
                onToggleMenu = { menuExpanded = it }
            )
        },
        bottomBar = {
            TeacherDashboardBottomNavigationBar(
                currentRoute = currentRoute,
                onDashboardClick = onDashboardClick,
                onDraftsClick = onDraftsClick,
                onArchiveClick = onArchiveClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item { WelcomeSection(userName = userName, mascotRes = mascotRes) }
                
                // Management Section
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = GreenMedium, shape = RoundedCornerShape(10.dp), modifier = Modifier.size(40.dp)) {
                                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Text("Management", fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = GreenDark)
                        }

                        Spacer(Modifier.height(8.dp))

                        ActionItem(icon = Icons.Default.Campaign, label = "Announcements", onClick = onManageAnnouncements)
                        ActionItem(icon = Icons.Default.Business, label = "Campus Info", onClick = onManageCampusInfo)
                        ActionItem(icon = Icons.Default.People,   label = "Students List", onClick = onViewStudents)
                    }
                }

                item {
                    // Stats Card
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD5FFDE)),
                        border = BorderStroke(1.dp, GreenDark)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                            StatRow("Total Students", totalStudents)
                            HorizontalDivider(color = Color(0xFF78C48A))
                            StatRow("Active Announcements", totalAnnouncements)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeacherDashboardTopBar(
    role: String,
    onLogout: () -> Unit,
    menuExpanded: Boolean,
    onToggleMenu: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(Brush.horizontalGradient(listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))))
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = role,
                color = GreenDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins
            )

            Box {
                IconButton(onClick = { onToggleMenu(true) }) {
                    Icon(Icons.Default.AccountCircle, "Profile", tint = GreenDark, modifier = Modifier.size(32.dp))
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { onToggleMenu(false) },
                    modifier = Modifier.background(Color.White).width(160.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Logout", fontFamily = Poppins, fontSize = 14.sp, color = Color(0xFFD32F2F)) },
                        leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp)) },
                        onClick = {
                            onToggleMenu(false)
                            onLogout()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TeacherDashboardBottomNavigationBar(
    currentRoute: String,
    onDashboardClick: () -> Unit,
    onDraftsClick: () -> Unit,
    onArchiveClick: () -> Unit,
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
            BottomNavItem(Icons.Default.Home, "Home", currentRoute == "dashboard", onDashboardClick)
            BottomNavItem(Icons.Default.Description, "Drafts", currentRoute == "drafts", onDraftsClick)
            BottomNavItem(Icons.Default.Archive, "Archive", currentRoute == "archive", onArchiveClick)
            BottomNavItem(Icons.Default.Settings, "Settings", currentRoute == "settings", onSettingsClick)
        }
    }
}

@Composable
private fun ActionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 6.dp).height(64.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F1)),
        border = BorderStroke(1.dp, Color(0xFFDDE9E1))
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(GreenLight), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = GreenDark, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(16.dp))
            Text(label, fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF2B4B49), modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = GreenDark)
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF245B3D))
        Text(value.toString(), fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = GreenDark)
    }
}
