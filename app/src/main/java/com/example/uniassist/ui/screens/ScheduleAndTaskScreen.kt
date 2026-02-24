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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.ui.theme.*

private val screenContentPadding = 16.dp

data class TaskItem(
    val id: Int,
    val title: String,
    val completed: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleAndTaskScreen(
    onDashboardClick: () -> Unit = {},
    onCampusClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val datePickerState = rememberDatePickerState()

    // --- STATE MANAGEMENT ---
    val taskList = remember {
        mutableStateListOf(
            TaskItem(1, "ITP103 Assignment", true),
            TaskItem(2, "Claim Clearance", true),
            TaskItem(3, "Capstone meeting", false)
        )
    }

    var taskInputText by remember { mutableStateOf("") }
    var editingTaskId by remember { mutableStateOf<Int?>(null) }

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(Icons.Default.Notifications, "Notifications", tint = Color(0xFF1B5E20), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.AccountCircle, "Profile", tint = Color(0xFF1B5E20), modifier = Modifier.size(28.dp))
                }
            }
        },
        containerColor = Color.White,
        bottomBar = {
            ScheduleBottomNavigationBar(
                onDashboardClick = onDashboardClick,
                onCampusClick = onCampusClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(screenContentPadding)
                .background(Color.White)
        ) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF9D40A6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CalendarMonth, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Text("Schedule & Tasks", color = DarkGreenText, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.height(screenContentPadding))

            CalendarCard(datePickerState = datePickerState)

            Spacer(modifier = Modifier.height(screenContentPadding))

            // Task input row
            TaskInputRow(
                taskText = taskInputText,
                onTaskTextChange = { taskInputText = it },
                buttonLabel = if (editingTaskId != null) "Save" else "Add",
                onAddClick = {
                    if (taskInputText.isNotBlank()) {
                        if (editingTaskId != null) {
                            val index = taskList.indexOfFirst { it.id == editingTaskId }
                            if (index != -1) taskList[index] = taskList[index].copy(title = taskInputText)
                            editingTaskId = null
                        } else {
                            val newId = (taskList.maxOfOrNull { it.id } ?: 0) + 1
                            taskList.add(TaskItem(newId, taskInputText, false))
                        }
                        taskInputText = ""
                    }
                }
            )

            Spacer(modifier = Modifier.height(screenContentPadding))

            // Task list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(taskList, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onCheckedChange = { isChecked ->
                            val index = taskList.indexOfFirst { it.id == task.id }
                            if (index != -1) taskList[index] = task.copy(completed = isChecked)
                        },
                        onEditClick = {
                            taskInputText = task.title
                            editingTaskId = task.id
                        },
                        onDeleteClick = { taskList.removeIf { it.id == task.id } }
                    )
                }
            }
        }
    }
}

// ---------------- MISSING COMPONENTS FIXED ----------------

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
    }
}

@Composable
fun ScheduleBottomNavigationBar(
    onDashboardClick: () -> Unit,
    onCampusClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(65.dp),
        shape = RoundedCornerShape(35.dp),
        color = Color(0xFF245B3D),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(Icons.Default.Home, "Dashboard", false, onDashboardClick)
            BottomNavItem(Icons.Default.CalendarMonth, "Schedules", true, {})
            BottomNavItem(Icons.Default.Info, "Campus", false, onCampusClick)
            BottomNavItem(Icons.Default.Settings, "Settings", false, onSettingsClick)
        }
    }
}

// ---------------- REUSABLE COMPONENTS ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarCard(datePickerState: DatePickerState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightGreyBackground)
            .border(2.dp, GreenAccent, RoundedCornerShape(12.dp))
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = LightGreyBackground,
                titleContentColor = DarkGreenText,
                selectedDayContainerColor = BottomNavGreen,
                todayContentColor = BottomNavGreen,
                todayDateBorderColor = GreenAccent
            ),
            title = null,
            headline = null,
            showModeToggle = false
        )
    }
}

@Composable
private fun TaskInputRow(
    taskText: String,
    onTaskTextChange: (String) -> Unit,
    onAddClick: () -> Unit,
    buttonLabel: String
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
                .border(1.dp, LightGreyBorder, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (taskText.isEmpty()) Text("Enter task", color = GreyText)
                innerTextField()
            }
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(BottomNavGreen)
                .clickable { onAddClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(buttonLabel, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TaskRow(
    task: TaskItem,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, GreenAccent, RoundedCornerShape(8.dp))
            .background(LightGreyBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = BottomNavGreen)
        )
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            textDecoration = if (task.completed) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, "Edit", tint = OrangeAccent)
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, "Delete", tint = RedAccent)
        }
    }
}