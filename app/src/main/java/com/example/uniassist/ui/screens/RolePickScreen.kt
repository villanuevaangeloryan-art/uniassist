package com.example.uniassist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    val selectedGreen = Color(0xFF2D9E60)
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.background_rolepick),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        navController.navigate("get_started")
                    },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                        .padding(8.dp)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color(0xFF1B5E3C),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Continue as",
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF31663F)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select your role to personalize your experience",
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontSize = 14.sp,
                color = Color(0xFF31663F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RoleCard(
                    title = "Student",
                    description = "View announcements,\nmanage tasks, track schedules",
                    imageRes = R.drawable.uniassist_student,
                    isSelected = selectedRole == "Student",
                    onClick = { selectedRole = "Student" },
                    modifier = Modifier.weight(1f),
                    selectedColor = selectedGreen
                )

                RoleCard(
                    title = "Teacher",
                    description = "Post announcements,\nmanage students,\nview reports",
                    imageRes = R.drawable.uniassist_teacher,
                    isSelected = selectedRole == "Teacher",
                    onClick = { selectedRole = "Teacher" },
                    modifier = Modifier.weight(1f),
                    selectedColor = selectedGreen
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick  = {
                    navController.navigate("login_register/$selectedRole")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF245B3D)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Continue",
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "By TechSpace",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.poppins_bold))
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
    selectedColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .height(350.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(15.dp),
            border = if (isSelected)
                BorderStroke(2.dp, selectedColor)
            else
                BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDF7)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(165.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = title,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color(0xFF31663F)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp,
                    color = Color(0xFF31663F)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { onClick() },
                    shape = RoundedCornerShape(18),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) selectedColor else Color.LightGray
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isSelected) selectedColor else Color.DarkGray,
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = if (isSelected) selectedColor else Color.DarkGray
                    )
                }
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = (-16).dp)
                    .size(26.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(27.5.dp)
                )
            }
        }
    }
}