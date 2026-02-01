package com.example.uniassist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.ui.theme.BottomNavGreen
import com.example.uniassist.ui.theme.DarkGreenText
import com.example.uniassist.ui.theme.GreenAccent
import com.example.uniassist.ui.theme.GreyText
import com.example.uniassist.ui.theme.HeaderGreenEnd
import com.example.uniassist.ui.theme.HeaderGreenStart
import com.example.uniassist.ui.theme.LightGreyBackground
import com.example.uniassist.ui.theme.LightGreyBorder
import com.example.uniassist.ui.theme.OrangeAccent
import com.example.uniassist.ui.theme.RedAccent
import java.util.Calendar

// Content padding used for the schedule screen body
private val screenContentPadding = 16.dp

// Data class for a single task
data class TaskItem(
    val id: Int,
    val title: String,
    val completed: Boolean
)

@Composable
fun ScheduleAndTaskScreen(
    onDashboardClick: () -> Unit = {},
    onSchedulesClick: () -> Unit = {},
    onCampusClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var selectedDate by remember { mutableIntStateOf(19) }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().apply { set(2026, Calendar.JANUARY, 1) }) }
    val taskList = remember {
        mutableStateListOf(
            TaskItem(1, "ITP103 Assignment", true),
            TaskItem(2, "Claim Clearance", true),
            TaskItem(3, "Capstone meeting", false)
        )
    }
    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                // Green gradient header with notification and profile icons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(HeaderGreenStart, HeaderGreenEnd)
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = DarkGreenText
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = DarkGreenText
                        )
                    }
                }
                // Title row: red calendar icon (like Campus logo) + "Schedule & Tasks"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RedAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Schedule & Tasks",
                        color = DarkGreenText,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        },
        containerColor = Color.White,
        bottomBar = {
            BottomNavBar(
                onDashboardClick = onDashboardClick,
                onSchedulesClick = onSchedulesClick,
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
            // Calendar card (design only - not clickable)
            CalendarCard(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelect = { },
                onPrevMonth = { },
                onNextMonth = { }
            )

            Spacer(modifier = Modifier.height(screenContentPadding))

            // Task input row (design only - not clickable)
            TaskInputRow(
                taskText = "",
                onTaskTextChange = { },
                onAddClick = { },
                enabled = false
            )

            Spacer(modifier = Modifier.height(screenContentPadding))

            // Task list (design only - not clickable)
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

@Composable
private fun CalendarCard(
    currentMonth: Calendar,
    selectedDate: Int,
    onDateSelect: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthNames = arrayOf("January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December")
    val month = currentMonth.get(Calendar.MONTH)
    val year = currentMonth.get(Calendar.YEAR)

    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK)
    // Calendar.SUNDAY = 1, so offset for "S M T W T F S" is firstDayOfWeek - 1
    val startOffset = firstDayOfWeek - 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightGreyBackground)
            .border(2.dp, GreenAccent, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Month/Year and arrows row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${monthNames[month]} $year",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select month",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
            Row {
                IconButton(onClick = onPrevMonth, enabled = false) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous month", tint = Color.Black)
                }
                IconButton(onClick = onNextMonth, enabled = false) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next month", tint = Color.Black)
                }
            }
        }

        // Days of week header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(text = day, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            }
        }

        // Date grid (6 rows x 7 columns)
        val totalCells = 42
        val days = (1..totalCells).map { cellIndex ->
            val dayNumber = cellIndex - startOffset
            if (dayNumber in 1..daysInMonth) dayNumber else null
        }

        days.chunked(7).forEach { weekRow ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weekRow.forEach { day ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day != null) {
                            val isSelected = day == selectedDate
                            val isTodayOrHighlight = day == 18 && selectedDate == 19
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .then(
                                        if (isSelected) Modifier.background(BottomNavGreen)
                                        else if (isTodayOrHighlight) Modifier.border(1.dp, GreenAccent, CircleShape)
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
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
            textStyle = androidx.compose.ui.text.TextStyle(
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

@Composable
private fun BottomNavBar(
    selectedTab: String = "Schedules",
    onDashboardClick: () -> Unit = {},
    onSchedulesClick: () -> Unit = {},
    onCampusClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .height(58.dp),
        shape = RoundedCornerShape(35.dp),
        color = BottomNavGreen,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "Dashboard",
                icon = Icons.Default.Home,
                isSelected = selectedTab == "Dashboard",
                onClick = onDashboardClick
            )
            NavItem(
                label = "Schedules",
                icon = Icons.Default.CalendarMonth,
                isSelected = selectedTab == "Schedules",
                onClick = onSchedulesClick
            )
            NavItem(
                label = "Campus",
                icon = Icons.Default.Info,
                isSelected = selectedTab == "Campus",
                onClick = onCampusClick
            )
            NavItem(
                label = "Settings",
                icon = Icons.Default.Settings,
                isSelected = selectedTab == "Settings",
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) DarkGreenText else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = if (isSelected) 12.sp else 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = Color.White
        )
    }
}


