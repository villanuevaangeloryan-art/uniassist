package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R

@Composable
fun StudentSettingsBottomNavigationBar(
    onDashboardClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onCampusClick: () -> Unit
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
            BottomNavItem(Icons.Default.Home, "Home", false, onDashboardClick)
            BottomNavItem(Icons.Default.DateRange, "Schedules", false, onScheduleClick)
            BottomNavItem(Icons.Default.Info, "Campus", false, onCampusClick)
            BottomNavItem(Icons.Default.Settings, "Settings", true, {})
        }
    }
}

@Composable
fun TeacherSettingsBottomNavigationBar(
    onDashboardClick: () -> Unit,
    onDraftsClick: () -> Unit,
    onArchiveClick: () -> Unit
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
            BottomNavItem(Icons.Default.Home, "Home", false, onDashboardClick)
            BottomNavItem(Icons.Default.Description, "Drafts", false, onDraftsClick)
            BottomNavItem(Icons.Default.Archive, "Archive", false, onArchiveClick)
            BottomNavItem(Icons.Default.Settings, "Settings", true, {})
        }
    }
}

@Composable
fun SettingsScreen(
    role: String = "Student",
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onCampusClick: () -> Unit = {},
    onDraftsClick: () -> Unit = {},
    onArchiveClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var showProfileMenu by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showNotificationSettingsDialog by remember { mutableStateOf(false) }
    var showPrivacySettingsDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showHelpSupportDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            SettingsHeaderSection(
                role = role,
                onProfileClick = { showProfileMenu = !showProfileMenu },
                showProfileMenu = showProfileMenu,
                onDismissMenu = { showProfileMenu = false },
                onEditProfileClick = {
                    showProfileMenu = false
                    showEditProfileDialog = true
                }
            )
        },
        bottomBar = {
            if (role == "Teacher") {
                TeacherSettingsBottomNavigationBar(
                    onDashboardClick = onDashboardClick,
                    onDraftsClick = onDraftsClick,
                    onArchiveClick = onArchiveClick
                )
            } else {
                StudentSettingsBottomNavigationBar(
                    onDashboardClick = onDashboardClick,
                    onScheduleClick = onScheduleClick,
                    onCampusClick = onCampusClick
                )
            }
        },
        containerColor = bgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(bgColor)
                .verticalScroll(scrollState)
        ) {
            SettingsTitleSection(isDarkMode = isDarkMode)

            SettingsContent(
                role = role,
                isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange,
                onNotificationSettingsClick = { showNotificationSettingsDialog = true },
                onPrivacySettingsClick = { showPrivacySettingsDialog = true },
                onChangePasswordClick = { showChangePasswordDialog = true },
                onAboutClick = { showAboutDialog = true },
                onHelpSupportClick = { showHelpSupportDialog = true }
            )

            Spacer(modifier = Modifier.height(24.dp))
            LogoutButton(onClick = { showLogoutDialog = true })
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // Dialogs Implementation
    if (showEditProfileDialog) EditProfileDialog(role = role, onDismiss = { showEditProfileDialog = false })
    if (showNotificationSettingsDialog) NotificationSettingsDialog(role = role, onDismiss = { showNotificationSettingsDialog = false })
    if (showPrivacySettingsDialog) PrivacySettingsDialog(onDismiss = { showPrivacySettingsDialog = false })
    if (showChangePasswordDialog) ChangePasswordDialog(onDismiss = { showChangePasswordDialog = false })
    if (showAboutDialog) AboutDialog(onDismiss = { showAboutDialog = false })
    if (showHelpSupportDialog) HelpSupportDialog(onDismiss = { showHelpSupportDialog = false })
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun SettingsHeaderSection(
    role: String,
    onProfileClick: () -> Unit = {},
    showProfileMenu: Boolean = false,
    onDismissMenu: () -> Unit = {},
    onEditProfileClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(Brush.horizontalGradient(colors = listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = role, color = Color(0xFF1B5E20), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (role == "Student") {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications", tint = Color(0xFF1B5E20), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Box {
                    IconButton(onClick = onProfileClick) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color(0xFF1B5E20), modifier = Modifier.size(32.dp))
                    }
                    DropdownMenu(expanded = showProfileMenu, onDismissRequest = onDismissMenu, modifier = Modifier.width(280.dp).background(Color.White)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFF66BB6A))) {
                                Image(painter = painterResource(id = R.drawable.profile), contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = if (role == "Teacher") "Prof. Angelo Villanueva" else "Juan Dela Cruz", color = Color(0xFF1B5E20), fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = if (role == "Teacher") "angelo.v@university.edu" else "juan.d@university.edu", color = Color(0xFF666666), fontSize = 12.sp, fontFamily = Poppins)
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(8.dp))
                            DropdownMenuItem(text = { Text("Edit Profile", color = Color(0xFF1B5E20), fontFamily = Poppins) }, onClick = onEditProfileClick, leadingIcon = { Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF1B5E20)) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsTitleSection(isDarkMode: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = Color(0xFF99C24D), shape = RoundedCornerShape(10.dp), modifier = Modifier.size(44.dp)) {
            Icon(Icons.Default.Settings, null, tint = Color.White, modifier = Modifier.padding(10.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Settings", color = if (isDarkMode) Color.White else Color(0xFF1B5E20), fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, fontFamily = Poppins)
    }
}

@Composable
fun SettingsContent(
    role: String,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onPrivacySettingsClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onAboutClick: () -> Unit,
    onHelpSupportClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (role == "Teacher") {
            SettingsOptionCard(icon = Icons.Default.Campaign, title = "Notification Broadcast", subtitle = "Manage global announcement alerts", isDarkMode = isDarkMode, onClick = onNotificationSettingsClick)
            SettingsOptionCard(icon = Icons.Default.ManageAccounts, title = "Account Permissions", subtitle = "Manage teacher administrative rights", isDarkMode = isDarkMode, onClick = {})
        } else {
            SettingsOptionCard(icon = Icons.Default.Notifications, title = "Notification Settings", subtitle = "Manage your notification preferences", isDarkMode = isDarkMode, onClick = onNotificationSettingsClick)
        }

        SettingsOptionCard(icon = Icons.Default.DarkMode, title = "Dark Mode", subtitle = "Toggle dark theme for the app", isDarkMode = isDarkMode, hasSwitch = true, switchState = isDarkMode, onSwitchChange = onDarkModeChange, onClick = { onDarkModeChange(!isDarkMode) })
        SettingsOptionCard(icon = Icons.Default.Security, title = "Privacy Settings", subtitle = "Control your privacy and data", isDarkMode = isDarkMode, onClick = onPrivacySettingsClick)

        if (role == "Student") {
            SettingsOptionCard(icon = Icons.Default.Lock, title = "Change Password", subtitle = "Update your account password", isDarkMode = isDarkMode, onClick = onChangePasswordClick)
        }
        
        SettingsOptionCard(icon = Icons.Default.Info, title = "About", subtitle = "App version and information", isDarkMode = isDarkMode, onClick = onAboutClick)
        SettingsOptionCard(icon = Icons.Default.Help, title = "Help & Support", subtitle = "Get help and contact support", isDarkMode = isDarkMode, onClick = onHelpSupportClick)
    }
}

@Composable
fun SettingsOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isDarkMode: Boolean = false,
    hasSwitch: Boolean = false,
    switchState: Boolean = false,
    onSwitchChange: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF1F8F1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = Color(0xFF1B5E20)) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp).padding(12.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = if (isDarkMode) Color.White else Color(0xFF1B5E20), fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle, color = if (isDarkMode) Color(0xFFBBBBBB) else Color(0xFF4C6B4F), fontSize = 12.sp, fontFamily = Poppins)
            }
            if (hasSwitch) {
                Switch(
                    checked = switchState,
                    onCheckedChange = onSwitchChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF1B5E20),
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = "Log Out", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = Poppins)
    }
}

// Implementations of functional dialogs
@Composable
fun EditProfileDialog(role: String, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(if (role == "Teacher") "Prof. Angelo Villanueva" else "Juan Dela Cruz") }
    var email by remember { mutableStateOf(if (role == "Teacher") "angelo.v@university.edu" else "juan.d@university.edu") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile", fontWeight = FontWeight.Bold, fontFamily = Poppins) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password", fontWeight = FontWeight.Bold, fontFamily = Poppins) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = oldPass, onValueChange = { oldPass = it }, label = { Text("Current Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = newPass, onValueChange = { newPass = it }, label = { Text("New Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Update") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Out", fontWeight = FontWeight.Bold, fontFamily = Poppins) },
        text = { Text("Are you sure you want to log out of UniAssist?", fontFamily = Poppins) },
        confirmButton = { Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))) { Text("Log Out", color = Color.White) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun NotificationSettingsDialog(role: String, onDismiss: () -> Unit) {
    var pushEnabled by remember { mutableStateOf(true) }
    var emailEnabled by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (role == "Teacher") "Broadcast Settings" else "Notifications", fontWeight = FontWeight.Bold, fontFamily = Poppins) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = pushEnabled, onCheckedChange = { pushEnabled = it })
                    Text("Push Notifications", fontFamily = Poppins)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = emailEnabled, onCheckedChange = { emailEnabled = it })
                    Text("Email Notifications", fontFamily = Poppins)
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Done") } }
    )
}

@Composable
fun PrivacySettingsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Privacy", fontWeight = FontWeight.Bold, fontFamily = Poppins) },
        text = { Text("Your data is encrypted and managed according to university policies. No student data is shared with third parties.", fontFamily = Poppins) },
        confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } }
    )
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About UniAssist", fontWeight = FontWeight.Bold, fontFamily = Poppins) },
        text = { Text("Version 1.0.2\nDeveloped for Case Study 2025.\nAn all-in-one assistant for university life.", fontFamily = Poppins) },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

@Composable
fun HelpSupportDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Help & Support", fontWeight = FontWeight.Bold, fontFamily = Poppins) },
        text = { Text("Email: support@university.edu\nPhone: (123) 456-7890\nOffice Hours: Mon-Fri, 8AM-5PM", fontFamily = Poppins) },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}
