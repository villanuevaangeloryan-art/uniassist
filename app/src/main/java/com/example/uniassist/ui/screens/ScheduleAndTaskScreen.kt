package com.example.uniassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.uniassist.BottomNavItem
import com.example.uniassist.ui.theme.BottomNavGreen
import com.example.uniassist.ui.theme.DarkGreenText
import com.example.uniassist.ui.theme.GreenAccent
import com.example.uniassist.ui.theme.GreyText
import com.example.uniassist.ui.theme.LightGreyBackground
import com.example.uniassist.ui.theme.LightGreyBorder
import com.example.uniassist.ui.theme.OrangeAccent
import com.example.uniassist.ui.theme.RedAccent


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
    //  Material3 DatePickerState
    val datePickerState = rememberDatePickerState()

    val taskList = remember {
        mutableStateListOf(
            TaskItem(1, "ITP103 Assignment", true),
            TaskItem(2, "Claim Clearance", true),
            TaskItem(3, "Capstone meeting", false)
        )
    }

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
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Schedule & Tasks",
                    color = DarkGreenText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(screenContentPadding))

            //  DATEPICKER COMPONENT
            CalendarCard(datePickerState = datePickerState)

            Spacer(modifier = Modifier.height(screenContentPadding))

            // Task input row
            TaskInputRow(
                taskText = "",
                onTaskTextChange = { },
                onAddClick = { },
                enabled = false
            )

            Spacer(modifier = Modifier.height(screenContentPadding))

            // Task list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(taskList, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onCheckedChange = { },
                        onEditClick = { },
                        onDeleteClick = { },
                        enabled = false
                    )
                }
            }
        }
    }
}

// ---------------- REUSABLE COMPONENTS ----------------

@Composable
fun ScheduleBottomNavigationBar(
    onDashboardClick: () -> Unit,
    onCampusClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
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
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Dashboard",
                isSelected = false,
                onClick = onDashboardClick
            )

            BottomNavItem(
                icon = Icons.Default.CalendarMonth,
                label = "Schedules",
                isSelected = true,
                onClick = {}
            )

            BottomNavItem(
                icon = Icons.Default.Info,
                label = "Campus",
                isSelected = false,
                onClick = onCampusClick
            )

            BottomNavItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                isSelected = false,
                onClick = onSettingsClick
            )
        }
    }
}

// ---------------- CALENDAR COMPONENTS ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarCard(
    datePickerState: DatePickerState
) {
    // wrap the DatePicker in  styled container
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightGreyBackground)
            .border(2.dp, GreenAccent, RoundedCornerShape(12.dp))

    ) {
        // Native Material3 DatePicker
        DatePicker(
            state = datePickerState,
            // Customizing colors to match theme
            colors = DatePickerDefaults.colors(
                containerColor = LightGreyBackground,
                titleContentColor = DarkGreenText,
                headlineContentColor = DarkGreenText,
                weekdayContentColor = DarkGreenText,
                subheadContentColor = DarkGreenText,
                navigationContentColor = DarkGreenText,
                yearContentColor = DarkGreenText,
                currentYearContentColor = BottomNavGreen,
                selectedYearContentColor = Color.White,
                selectedYearContainerColor = BottomNavGreen,
                dayContentColor = Color.Black,
                disabledDayContentColor = GreyText,
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = BottomNavGreen,
                todayDateBorderColor = GreenAccent,
                todayContentColor = BottomNavGreen
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
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicTextField(
            value = taskText,
            onValueChange = onTaskTextChange,
            readOnly = !enabled,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, LightGreyBorder, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            decorationBox = { innerTextField ->
                if (taskText.isEmpty()) {
                    Text(
                        "Enter task",
                        color = GreyText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                innerTextField()
            }
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(BottomNavGreen)
                .then(
                    if (enabled) Modifier.clickable(onClick = onAddClick)
                    else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun TaskRow(
    task: TaskItem,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    enabled: Boolean = true
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
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = BottomNavGreen,
                uncheckedColor = LightGreyBorder
            )
        )
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textDecoration = if (task.completed) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onEditClick, enabled = enabled) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                tint = OrangeAccent,
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = onDeleteClick, enabled = enabled) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = RedAccent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}