package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uniassist.R


val PrimaryGreen = Color(0xFF245B3D)
val DarkGreenTint = Color(0xFF1B5E3C)
val FooterGray = Color(0xFF898989)

@Composable
fun RegisterScreen(navController: NavController) {
    // Form State
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val poppinsBold = FontFamily(Font(R.font.poppins_bold))
    val poppinsMedium = FontFamily(Font(R.font.poppins_medium))

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDEF8CB).copy(alpha = 0.5f))
        )

        // Content Layer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Back Button
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
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

                // Logo
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.gts_uniassist_logo),
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
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Form Fields
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
            AuthTextField(email, { email = it }, "Enter your email", keyboardType = KeyboardType.Email)

            Spacer(modifier = Modifier.height(16.dp))
            AuthLabel("Username", poppinsBold)
            AuthTextField(username, { username = it }, "Enter username")

            Spacer(modifier = Modifier.height(16.dp))
            AuthLabel("Password", poppinsBold)
            AuthTextField(password, { password = it }, "Enter password", isPassword = true)

            Spacer(modifier = Modifier.height(16.dp))
            AuthLabel("Confirm Password", poppinsBold)
            AuthTextField(confirmPassword, { confirmPassword = it }, "Confirm password", isPassword = true)

            Spacer(modifier = Modifier.height(40.dp))

            // Register Button
            Button(
                onClick = {
                },
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

            // Login Link
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
                fontFamily = poppinsMedium,
                modifier = Modifier.clickable {
                    // Navigate to login
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Footer
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

//Composables

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color(0xFFA0A0A0),
            focusedBorderColor = PrimaryGreen,
            cursorColor = PrimaryGreen,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}

@Composable
fun AuthLabel(text: String, fontFamily: FontFamily) {
    Text(
        text = text,
        fontFamily = fontFamily,
        color = PrimaryGreen,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, start = 2.dp)
    )
}