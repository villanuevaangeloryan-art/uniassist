package com.example.uniassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.ui.theme.*
import com.example.uniassist.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

private val screenContentPadding = 16.dp

data class TaskItem(
    val id: Int,
    val title: String,
    val dateKey: String,
    val completed: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleAndTaskScreen(
    // MVVM
    taskViewModel: TaskViewModel,
    onDashboardClick: () -> Unit = {},
    onCampusClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val datePickerState = rememberDatePickerState()
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    // Derive the selected date key from the calendar picker.
    val selectedDateKey by remember(datePickerState.selectedDateMillis) {
        derivedStateOf {
            val millis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
        }
    }

    // Tell the ViewModel which date is selected so it queries Room for that day.
    LaunchedEffect(selectedDateKey) {
        taskViewModel.setSelectedDate(selectedDateKey)
    }

    // Collect the live task list from Room via StateFlow.
    val tasksForSelectedDate by taskViewModel.tasksForSelectedDate.collectAsState()

    var taskInputText by remember { mutableStateOf("") }
    var editingTask   by remember { mutableStateOf<TaskItem?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.Center),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(Icons.Default.Notifications, "Notifications", tint = Color(0xFF1B5E20), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.AccountCircle, "Profile", tint = Color(0xFF1B5E20), modifier = Modifier.size(28.dp))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ScheduleBottomNavigationBar(
                onDashboardClick = onDashboardClick,
                onCampusClick = onCampusClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = screenContentPadding)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(screenContentPadding),
            contentPadding = PaddingValues(
                top = screenContentPadding,
                bottom = screenContentPadding + 100.dp
            )
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF9D40A6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarMonth, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Text("Schedule & Tasks", color = if (isDark) MaterialTheme.colorScheme.primary else DarkGreenText, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                }
            }

            item {
                CalendarCard(datePickerState = datePickerState)
            }

            item {
                val selectedDateText = remember(datePickerState.selectedDateMillis) {
                    val millis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(millis))
                }
                Text(
                    text = "Tasks for $selectedDateText",
                    color = if (isDark) MaterialTheme.colorScheme.onSurface else DarkGreenText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                TaskInputRow(
                    taskText = taskInputText,
                    onTaskTextChange = { taskInputText = it },
                    buttonLabel = if (editingTask != null) "Save" else "Add",
                    onAddClick = {
                        if (taskInputText.isNotBlank()) {
                            if (editingTask != null) {
                                // Update existing task title in Room.
                                taskViewModel.updateTask(editingTask!!.copy(title = taskInputText))
                                editingTask = null
                            } else {
                                // Add new task to Room for the selected date.
                                taskViewModel.addTask(taskInputText)
                            }
                            taskInputText = ""
                        }
                    },
                    isDark = isDark
                )
            }

            if (tasksForSelectedDate.isEmpty()) {
                item {
                    Text(
                        text = "No tasks for this day yet.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                item {
                    val completedCount = tasksForSelectedDate.count { it.completed }
                    val totalCount     = tasksForSelectedDate.size
                    Text(
                        text = "$completedCount of $totalCount tasks completed",
                        color = if (isDark) MaterialTheme.colorScheme.secondary else DarkGreenText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                items(tasksForSelectedDate, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onCheckedChange = {
                            // Toggle persisted directly to Room.
                            taskViewModel.toggleComplete(task)
                        },
                        onEditClick = {
                            taskInputText = task.title
                            editingTask = task
                        },
                        onDeleteClick = {
                            // Delete from Room — list updates automatically.
                            taskViewModel.deleteTask(task.id)
                        },
                        isDark = isDark
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleBottomNavigationBar(
    onDashboardClick: () -> Unit,
    onCampusClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(64.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color(0xFF1B5E20),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(Icons.Default.Home,          "Home", false, onDashboardClick)
            BottomNavItem(Icons.Default.CalendarMonth, "Schedules", true,  {})
            BottomNavItem(Icons.Default.Info,          "Campus",    false, onCampusClick)
            BottomNavItem(Icons.Default.Settings,      "Settings",  false, onSettingsClick)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarCard(datePickerState: DatePickerState) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDark) Color(0xFF2C2C2C) else LightGreyBackground)
            .border(2.dp, if (isDark) MaterialTheme.colorScheme.primary else GreenAccent, RoundedCornerShape(12.dp))
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = if (isDark) Color(0xFF2C2C2C) else Color.White,
                titleContentColor = if (isDark) Color.White else DarkGreenText,
                headlineContentColor = if (isDark) Color.White else DarkGreenText,
                weekdayContentColor = if (isDark) Color.LightGray else DarkGreenText,
                subheadContentColor = if (isDark) Color.LightGray else DarkGreenText,
                dayContentColor = if (isDark) Color.White else DarkGreenText,
                disabledDayContentColor = Color.Gray,
                selectedDayContainerColor = if (isDark) MaterialTheme.colorScheme.primary else GreenAccent,
                selectedDayContentColor = Color.Black,
                todayDateBorderColor = if (isDark) MaterialTheme.colorScheme.primary else GreenAccent,
                dayInSelectionRangeContainerColor = GreenAccent.copy(alpha = 0.3f),
                dayInSelectionRangeContentColor = if (isDark) Color.White else DarkGreenText
            )
        )
    }
}


@Composable
private fun TaskInputRow(
    taskText: String,
    onTaskTextChange: (String) -> Unit,
    onAddClick: () -> Unit,
    buttonLabel: String,
    isDark: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicTextField(
            value = taskText,
            onValueChange = onTaskTextChange,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, if (isDark) Color.DarkGray else LightGreyBorder, RoundedCornerShape(8.dp))
                .background(if (isDark) Color(0xFF2C2C2C) else Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            singleLine = true,
            textStyle = TextStyle(color = if (isDark) Color.White else Color.Black, fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (taskText.isEmpty()) Text("Add a task for this day", color = Color.Gray)
                innerTextField()
            }
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDark) MaterialTheme.colorScheme.primary else BottomNavGreen)
                .clickable { onAddClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(buttonLabel, color = if (isDark) Color.Black else Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
private fun TaskRow(
    task: TaskItem,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isDark: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, if (isDark) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else GreenAccent, RoundedCornerShape(8.dp))
            .background(if (isDark) Color(0xFF2C2C2C) else LightGreyBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = if (isDark) MaterialTheme.colorScheme.primary else BottomNavGreen)
        )
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            color = if (task.completed) Color.Gray else if (isDark) Color.White else DarkGreenText,
            textDecoration = if (task.completed) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, "Edit", tint = if (isDark) MaterialTheme.colorScheme.primary else OrangeAccent)
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, "Delete", tint = if (isDark) Color(0xFFEF5350) else RedAccent)
        }
    }
}
