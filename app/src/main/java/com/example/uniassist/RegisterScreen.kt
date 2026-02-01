package com.example.uniassist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val poppinsBold = FontFamily.Default // Or FontFamily.SansSerif

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDEF8CB).copy(alpha = 0.5f))
        )

        // Layer of the Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Back Button Icon
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkGreenTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Logo of Uniassist
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(50.dp)
                            .scale(1.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title Section
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(70.dp)
                        .background(PrimaryGreen, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Join now !",
                        fontSize = 28.sp,
                        fontFamily = poppinsBold,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Register and experience handy features\nof the app. Please register to continue.",
                        fontSize = 13.sp,
                        color = PrimaryGreen,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Row for Names
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    AuthLabel("First Name", poppinsBold)
                    AuthTextField(firstName, { firstName = it }, "Enter first name")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    AuthLabel("Last Name", poppinsBold)
                    AuthTextField(lastName, { lastName = it }, "Enter last name")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            AuthLabel("Email", poppinsBold)
            AuthTextField(email, { email = it }, "Enter your email")

            Spacer(modifier = Modifier.height(16.dp))
            AuthLabel("Username", poppinsBold)
            AuthTextField(username, { username = it }, "Enter username")

            Spacer(modifier = Modifier.height(16.dp))
            AuthLabel("Password", poppinsBold)
            AuthTextField(password, { password = it }, "Enter password", isPassword = true)

            Spacer(modifier = Modifier.height(16.dp))
            AuthLabel("Confirm Password", poppinsBold)
            AuthTextField(confirmPassword, { confirmPassword = it }, "Enter password", isPassword = true)

            Spacer(modifier = Modifier.height(40.dp))

            // Register button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                elevation = ButtonDefaults.buttonElevation(1.dp)
            ) {
                Text(
                    text = "Register",
                    fontSize = 19.sp,
                    fontFamily = poppinsBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login link if you dont have an account
            val annotatedString = buildAnnotatedString {
                append("Already have an account? ")
                withStyle(style = SpanStyle(color = PrimaryGreen, fontFamily = poppinsBold)) {
                    append("Log in here.")
                }
            }
            Text(
                text = annotatedString,
                fontSize = 12.sp,
                color = PrimaryGreen,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Footer for techspace
            Text(
                text = "By TechSpace",
                fontSize = 13.sp,
                fontFamily = poppinsBold,
                color = FooterGray,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}