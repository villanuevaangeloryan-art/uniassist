package com.example.uniassist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uniassist.R

@Composable
fun RolePickScreen(
    navController: NavController
) {
    var selectedRole by remember { mutableStateOf("Student") }
    val scrollState = rememberScrollState()
    val primaryGreen = Color(0xFF1B5E3C)

    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ Background Image
        Image(
            painter = painterResource(id = R.drawable.background_rolepick),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ Back Arrow Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Surface(
                    onClick = { navController.popBackStack() },
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = primaryGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // ✅ Title
            Text(
                text = "Continue as",
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryGreen
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Subtitle
            Text(
                text = "Select your role to personalize your experience",
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontSize = 15.sp,
                color = primaryGreen.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ✅ Role Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RoleCard(
                    title = "Student",
                    description = "View announcements,\nmanage tasks, track\nschedules",
                    imageRes = R.drawable.uniassist_student,
                    isSelected = selectedRole == "Student",
                    onClick = { selectedRole = "Student" },
                    modifier = Modifier.weight(1f)
                )

                RoleCard(
                    title = "Teacher",
                    description = "Post announcements,\nmanage students,\nview reports",
                    imageRes = R.drawable.uniassist_teacher,
                    isSelected = selectedRole == "Teacher",
                    onClick = { selectedRole = "Teacher" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ✅ Continue Button
            Button(
                onClick = { navController.navigate("login_register/$selectedRole") },
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Continue",
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "By TechSpace",
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF2E5940)
    val cardBackground = Color(0xFFF4F8F4).copy(alpha = 0.95f)

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .then(
                    if (isSelected) Modifier.border(2.dp, primaryGreen, RoundedCornerShape(20.dp))
                    else Modifier
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(130.dp)
                        .padding(bottom = 12.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = title,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 20.sp,
                    color = primaryGreen
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    color = primaryGreen.copy(alpha = 0.7f),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isSelected) primaryGreen else Color.LightGray),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = if (isSelected) primaryGreen else Color.Gray,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )
                }
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
                    .background(Color(0xFF4CAF50), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}