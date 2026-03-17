package com.example.uniassist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

private val GreenDark = Color(0xFF1B5E20)
private val GreenMedium = Color(0xFF56A566)
private val BackgroundCream = Color(0xFFFDFDF8)

// Data class carrying the form fields back to the caller.
data class NewAnnouncement(
    val title: String,
    val category: String,
    val isPriority: Boolean,
    val message: String,
    val postDate: String,
    val endDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnnouncementScreen(
    onBackClick: () -> Unit = {},
    onSaveDraft: (NewAnnouncement) -> Unit = {},
    // onPublish: caller adds the item to the shared list then navigates back.
    onPublish: (NewAnnouncement) -> Unit = {}
) {
    var title    by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Emergency") }
    var isPriority by remember { mutableStateOf(true) }
    var message  by remember { mutableStateOf("") }

    var postDate by remember { mutableStateOf(Date()) }
    var endDate  by remember { mutableStateOf(Date()) }

    var showPostDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker  by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) }

    var profileMenuExpanded by remember { mutableStateOf(false) }

    val isPublishEnabled = title.isNotBlank() && message.isNotBlank()

    Scaffold(containerColor = BackgroundCream) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF7CA786), Color(0xFFE2F1C6)))
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GreenDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        "Create Announcement",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = GreenDark
                    )
                    Box {
                        IconButton(onClick = { profileMenuExpanded = true }) {
                            Box(
                                modifier = Modifier.size(34.dp).border(2.dp, GreenDark, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountCircle, "Profile", tint = GreenDark, modifier = Modifier.size(30.dp))
                            }
                        }
                        DropdownMenu(
                            expanded = profileMenuExpanded,
                            onDismissRequest = { profileMenuExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Drafts", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Description, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Archive, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                // Title
                InputLabel("Title")
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Enter announcement title", color = Color.LightGray, fontSize = 14.sp) },
                    textStyle = TextStyle(fontFamily = Poppins, fontSize = 14.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenMedium,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category
                InputLabel("Category")
                CategoryDropdownMenu(selectedCategory = category, onCategorySelected = { category = it })

                Spacer(modifier = Modifier.height(16.dp))

                // Priority
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputLabel("Priority", modifier = Modifier.padding(0.dp))
                    Switch(
                        checked = isPriority,
                        onCheckedChange = { isPriority = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = GreenMedium,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Message
                InputLabel("Message")
                RichTextEditor(message = message, onMessageChange = { message = it })

                Spacer(modifier = Modifier.height(24.dp))

                // Post Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InputLabel("Post Date", modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = dateFormatter.format(postDate),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp)
                            .clickable { showPostDatePicker = true },
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontFamily = Poppins, fontSize = 14.sp),
                        trailingIcon = {
                            IconButton(onClick = { showPostDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, "Pick post date", modifier = Modifier.size(18.dp), tint = GreenDark)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // End Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InputLabel("End Date", modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = dateFormatter.format(endDate),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp)
                            .clickable { showEndDatePicker = true },
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(fontFamily = Poppins, fontSize = 14.sp),
                        trailingIcon = {
                            IconButton(onClick = { showEndDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, "Pick end date", modifier = Modifier.size(18.dp), tint = GreenDark)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ── Action Buttons ─────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            onSaveDraft(
                                NewAnnouncement(title, category, isPriority, message,
                                    dateFormatter.format(postDate), dateFormatter.format(endDate))
                            )
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5E3E)),
                        enabled = title.isNotBlank()
                    ) {
                        Text("SAVE DRAFT", fontFamily = Poppins, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            // Pass the completed announcement up to the caller.
                            // The caller (NavGraph) will add it to the shared list
                            // and then call navController.popBackStack() so the user
                            // lands back on ManageAnnouncementsScreen with the new item.
                            onPublish(
                                NewAnnouncement(title, category, isPriority, message,
                                    dateFormatter.format(postDate), dateFormatter.format(endDate))
                            )
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                        enabled = isPublishEnabled
                    ) {
                        Text("PUBLISH", fontFamily = Poppins, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Post Date Picker Dialog
    if (showPostDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = postDate.time)
        DatePickerDialog(
            onDismissRequest = { showPostDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { postDate = Date(it) }
                    showPostDatePicker = false
                }) { Text("OK", color = Color(0xFF245B3D)) }
            },
            dismissButton = {
                TextButton(onClick = { showPostDatePicker = false }) {
                    Text("Cancel", color = Color(0xFF666666))
                }
            }
        ) {
            DatePicker(
                state = state,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(0xFF245B3D),
                    todayContentColor = Color(0xFF245B3D),
                    todayDateBorderColor = Color(0xFF245B3D)
                )
            )
        }
    }

    // End Date Picker Dialog
    if (showEndDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = endDate.time)
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { endDate = Date(it) }
                    showEndDatePicker = false
                }) { Text("OK", color = Color(0xFF245B3D)) }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancel", color = Color(0xFF666666))
                }
            }
        ) {
            DatePicker(
                state = state,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(0xFF245B3D),
                    todayContentColor = Color(0xFF245B3D),
                    todayDateBorderColor = Color(0xFF245B3D)
                )
            )
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun InputLabel(text: String, modifier: Modifier = Modifier.padding(bottom = 8.dp)) {
    Text(text = text, fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = GreenDark, modifier = modifier)
}

@Composable
private fun CategoryDropdownMenu(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var activeSubMenu by remember { mutableStateOf<String?>(null) }

    val categories = mapOf(
        "Academic"  to listOf("Exams", "Enrollment", "Deadlines", "Scholarship updates"),
        "Event"     to listOf("Campus festival", "Org activities", "Sports fest"),
        "General"   to listOf("Office hours", "Policy reminders", "General updates"),
        "Emergency" to listOf("Class suspension", "Typhoon alerts", "Safety notices")
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(fontFamily = Poppins, fontSize = 14.sp),
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, tint = GreenMedium) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenMedium,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        Box(modifier = Modifier.matchParentSize().clickable { expanded = true })

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false; activeSubMenu = null },
            modifier = Modifier.background(Color.White)
        ) {
            if (activeSubMenu == null) {
                categories.keys.forEach { mainCat ->
                    DropdownMenuItem(
                        text = { Text(mainCat, fontFamily = Poppins, color = Color.DarkGray) },
                        trailingIcon = { Icon(Icons.Default.ChevronRight, null, tint = GreenMedium) },
                        onClick = { activeSubMenu = mainCat }
                    )
                }
            } else {
                DropdownMenuItem(
                    text = { Text("← Back to Categories", fontFamily = Poppins, color = Color.Gray, fontSize = 12.sp) },
                    onClick = { activeSubMenu = null }
                )
                HorizontalDivider()
                categories[activeSubMenu]?.forEach { subCat ->
                    DropdownMenuItem(
                        text = { Text(subCat, fontFamily = Poppins, color = Color.DarkGray) },
                        onClick = {
                            onCategorySelected(subCat)
                            expanded = false
                            activeSubMenu = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RichTextEditor(message: String, onMessageChange: (String) -> Unit) {
    var isToolbarExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        BasicTextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.fillMaxSize().padding(bottom = 40.dp),
            textStyle = TextStyle(fontFamily = Poppins, fontSize = 14.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                if (message.isEmpty()) {
                    Text("Enter the announcement details here . . .", fontFamily = Poppins, color = Color.LightGray, fontSize = 14.sp)
                }
                innerTextField()
            }
        )
        Row(modifier = Modifier.align(Alignment.BottomStart), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                onClick = { isToolbarExpanded = !isToolbarExpanded },
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color.Gray),
                color = Color.White
            ) {
                Text("Aa", fontFamily = Poppins, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            AnimatedVisibility(
                visible = isToolbarExpanded,
                enter = expandHorizontally(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkHorizontally(animationSpec = tween(300)) + fadeOut()
            ) {
                Surface(
                    modifier = Modifier.padding(start = 8.dp),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    color = Color.White
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("B", fontFamily = Poppins, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { })
                        Text("I", fontFamily = Poppins, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, modifier = Modifier.clickable { })
                        Text("U", fontFamily = Poppins, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline, modifier = Modifier.clickable { })
                        Text("H1", fontFamily = Poppins, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { })
                        Text("H2", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { })
                        Text("H3", fontFamily = Poppins, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { })
                        Text("H4", fontFamily = Poppins, modifier = Modifier.clickable { })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateAnnouncementPreview() {
    MaterialTheme { CreateAnnouncementScreen() }
}