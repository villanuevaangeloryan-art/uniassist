package com.example.uniassist.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCampusDepartmentScreen(
    department: Department,
    onBackClick: () -> Unit = {},
    onUpdate: (Department) -> Unit = {}
) {
    var departmentName by remember { mutableStateOf(department.name) }
    var representative by remember { mutableStateOf(department.representative) }
    var email by remember { mutableStateOf(department.email) }
    var phone by remember { mutableStateOf(department.phone) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var postDate by remember { mutableStateOf(Date()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            EditDepartmentHeaderSection(onBackClick = onBackClick)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Department Name Field
            Text(
                text = "Department",
                color = Color(0xFF1B5E20),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = departmentName,
                onValueChange = { departmentName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter Department", color = Color(0xFFBDBDBD)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF66BB6A),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Department Representative Field
            Text(
                text = "Department Representative",
                color = Color(0xFF1B5E20),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = representative,
                onValueChange = { representative = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter Department Representative", color = Color(0xFFBDBDBD)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF66BB6A),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Email Field
            Text(
                text = "Email",
                color = Color(0xFF1B5E20),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter email", color = Color(0xFFBDBDBD)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF66BB6A),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Phone Number Field
            Text(
                text = "Phone Number",
                color = Color(0xFF1B5E20),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter phone number", color = Color(0xFFBDBDBD)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF66BB6A),
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Image Upload Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                    .clickable { imagePickerLauncher.launch("image/*") }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        "Choose photo",
                        color = Color(0xFF1B5E20),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (selectedImageUri != null) "Photo selected" else "No photo selected",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }

            // Post Date Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Post Date",
                    color = Color(0xFF1B5E20),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFBDBDBD)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = dateFormatter.format(postDate),
                        color = Color(0xFF1B5E20),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        tint = Color(0xFF1B5E20),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Update Button
            Button(
                onClick = {
                    val updatedDept = Department(
                        name = departmentName,
                        representative = representative,
                        email = email,
                        phone = phone,
                        logoRes = department.logoRes
                    )
                    onUpdate(updatedDept)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B5E20)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = departmentName.isNotBlank() && representative.isNotBlank() &&
                        email.isNotBlank() && phone.isNotBlank()
            ) {
                Text(
                    text = "UPDATE",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = postDate.time
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                postDate = Date(it)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK", color = Color(0xFF245B3D))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel", color = Color(0xFF666666))
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = Color(0xFF245B3D),
                        todayContentColor = Color(0xFF245B3D),
                        todayDateBorderColor = Color(0xFF245B3D)
                    )
                )
            }
        }
    }
}

@Composable
fun EditDepartmentHeaderSection(onBackClick: () -> Unit) {
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
                    text = "Edit Department",
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
