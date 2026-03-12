package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCampusDepartmentScreen(
    onBackClick: () -> Unit = {},
    onAddDepartmentClick: () -> Unit = {},
    onEditDepartmentClick: (Int) -> Unit = {}
) {
    var departments by remember {
        mutableStateOf(
            listOf(
                Department(
                    name = "College of Computer Studies / Information Technology",
                    representative = "Mr. Alvin M. Dimaunahan",
                    email = "alvinccsdimauanahan@gmail.com",
                    phone = "+639829901987",
                    logoRes = R.drawable.ccsdeptlogo
                ),
                Department(
                    name = "College of Engineering",
                    representative = "Ms. Jennelyn V. Tan",
                    email = "tanjennelyncoe@gmail.com",
                    phone = "+639549932855",
                    logoRes = R.drawable.coedeptlogo
                ),
                Department(
                    name = "College of Business Administration / Accountancy",
                    representative = "Ms. Vilma R. Cacho",
                    email = "rcachocbaa@gmail.com",
                    phone = "+639549932855",
                    logoRes = R.drawable.cbaadeptlogo
                ),
                Department(
                    name = "College of Education",
                    representative = "Ms. George C. Bacsa",
                    email = "geocalmacoedue@gmail.com",
                    phone = "+639877718241",
                    logoRes = R.drawable.coedudeptlogo
                )
            )
        )
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedDepartment by remember { mutableStateOf<Department?>(null) }
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(departments.size) { index ->
                val dept = departments[index]
                ManageDepartmentCard(
                    department = dept,
                    isMenuExpanded = expandedMenuIndex == index,
                    onMenuClick = { expandedMenuIndex = if (expandedMenuIndex == index) null else index },
                    onEdit = {
                        onEditDepartmentClick(index)
                        expandedMenuIndex = null
                    },
                    onDelete = {
                        departments = departments.filterIndexed { i, _ -> i != index }
                        expandedMenuIndex = null
                    },
                    onArchive = {
                        expandedMenuIndex = null
                    }
                )
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
                        imageVector = Icons.Default.ArrowBack,
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

@Composable
fun EditDepartmentDialog(
    department: Department,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(department.name) }
    var representative by remember { mutableStateOf(department.representative) }
    var email by remember { mutableStateOf(department.email) }
    var phone by remember { mutableStateOf(department.phone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Department", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Department Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = representative,
                    onValueChange = { representative = it },
                    label = { Text("Representative") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name, representative, email, phone) },
                enabled = name.isNotBlank() && representative.isNotBlank() && 
                         email.isNotBlank() && phone.isNotBlank()
            ) {
                Text("Save", color = Color(0xFF245B3D))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF666666))
            }
        }
    )
}
