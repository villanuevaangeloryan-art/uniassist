package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import com.example.uniassist.ui.viewmodel.CampusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCampusDepartmentScreen(
    viewModel: CampusViewModel,
    onBackClick: () -> Unit = {},
    onAddDepartmentClick: () -> Unit = {},
    onEditDepartmentClick: (Int) -> Unit = {}
) {
    // 1. COLLECT DATA FROM THE DATABASE
    val dbDepartments by viewModel.departments.collectAsState()

    // 2. Map the Database Entity to your UI Data Class with LOGO LOGIC
    val uiDepartments = dbDepartments.map { entity ->
        val details = entity.description.split(" | ")
        val rep = details.getOrElse(0) { "Unknown Rep" }
        val email = details.getOrElse(1) { "No Email" }
        val phone = details.getOrElse(2) { "No Phone" }

        // --- THE MISSING LOGO LOGIC HAS BEEN ADDED HERE ---
        val rawName = entity.departmentName.lowercase()
        val correctLogo = when {
            rawName.contains("engineering") || rawName.contains("coe") -> R.drawable.coedeptlogo
            rawName.contains("business") || rawName.contains("accountancy") || rawName.contains("cbaa") -> R.drawable.cbaadeptlogo
            rawName.contains("education") || rawName.contains("coed") -> R.drawable.coedudeptlogo
            else -> R.drawable.ccsdeptlogo
        }

        Department(
            name = entity.departmentName,
            representative = rep,
            email = email,
            phone = phone,
            logoRes = correctLogo // <-- NO LONGER HARDCODED!
        )
    }

    var expandedMenuIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            ManageCampusHeaderSection(onBackClick = onBackClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDepartmentClick,
                containerColor = Color(0xFF245B3D),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Department")
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->

        // Show a message if the database is empty
        if (uiDepartments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No departments found. Click + to add one!", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiDepartments.size) { index ->
                    val dept = uiDepartments[index]
                    ManageDepartmentCard(
                        department = dept,
                        isMenuExpanded = expandedMenuIndex == index,
                        onMenuClick = { expandedMenuIndex = if (expandedMenuIndex == index) null else index },
                        onEdit = {
                            onEditDepartmentClick(dbDepartments[index].id)
                            expandedMenuIndex = null
                        },
                        onDelete = {
                            // --- UNCOMMENTED: THIS NOW TELLS THE DATABASE TO DELETE ---
                            viewModel.deleteDepartment(dbDepartments[index].id)
                            expandedMenuIndex = null
                        },
                        onArchive = {
                            // Temporary: mapping archive to delete since there is no archive boolean yet
                            viewModel.deleteDepartment(dbDepartments[index].id)
                            expandedMenuIndex = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ManageCampusHeaderSection(onBackClick: () -> Unit) {
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
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1B5E20),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Campus Information",
                    color = Color(0xFF1B5E20),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ManageDepartmentCard(
    department: Department,
    isMenuExpanded: Boolean,
    onMenuClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onArchive: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color(0xFF66BB6A), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = department.logoRes),
                    contentDescription = "Department Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = department.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Department representative: ${department.representative}",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "Email: ${department.email}",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "Phone number: ${department.phone}",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
                Box {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color(0xFF1B5E20)
                        )
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = onMenuClick
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            leadingIcon = { Icon(Icons.Default.Edit, null, tint = Color(0xFF1B5E20)) },
                            onClick = onEdit
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color(0xFFD32F2F)) },
                            onClick = onDelete
                        )
                        DropdownMenuItem(
                            text = { Text("Archive") },
                            leadingIcon = { Icon(Icons.Default.Archive, null, tint = Color(0xFF666666)) },
                            onClick = onArchive
                        )
                    }
                }
            }
        }
    }
}