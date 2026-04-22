package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uniassist.R
import com.example.uniassist.data.FirebaseAuthRepository
import com.example.uniassist.ui.theme.BottomNavGreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Added 'private' to prevent clashing with other files!
private val PrimaryGreen = Color(0xFF245B3D)

@Composable
fun CompleteProfileScreen(
    navController: NavController,
    email: String,
    role: String
) {
    val scope = rememberCoroutineScope()
    val repository = remember { FirebaseAuthRepository() }

    val poppinsBold = FontFamily(Font(R.font.poppins_bold))
    val poppinsRegular = FontFamily(Font(R.font.poppins_regular))

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Wrap the screen in a Box to layer the background image behind the content
    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_rolepick),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // The form content
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Removed the solid White background here so the image shows through
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .background(Color.White, shape = CircleShape)
                        .size(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = BottomNavGreen)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            Text("Almost there!", fontSize = 28.sp, fontFamily = poppinsBold, color = PrimaryGreen)
            Text("Step 2: Set up your profile and app password.", color = Color.Gray, fontSize = 14.sp, fontFamily = poppinsRegular)

            Spacer(modifier = Modifier.height(30.dp))

            AuthLabel("First Name", poppinsBold)
            AuthTextFieldWithValidation(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "Enter first name",
                isError = false,
                errorMessage = "",
                poppinsRegular = poppinsRegular
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthLabel("Last Name", poppinsBold)
            AuthTextFieldWithValidation(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Enter last name",
                isError = false,
                errorMessage = "",
                poppinsRegular = poppinsRegular
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthLabel("Set App Password", poppinsBold)
            AuthTextFieldWithValidation(
                value = password,
                onValueChange = { password = it },
                placeholder = "Create a password",
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                isError = password.isNotEmpty() && password.length < 6,
                errorMessage = "Password must be at least 6 characters",
                poppinsRegular = poppinsRegular
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthLabel("Confirm Password", poppinsBold)
            AuthTextFieldWithValidation(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Retype your password",
                isPassword = true,
                isPasswordVisible = isConfirmPasswordVisible,
                onTogglePasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                isError = confirmPassword.isNotEmpty() && password != confirmPassword,
                errorMessage = "Passwords do not match",
                poppinsRegular = poppinsRegular
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = Color(0xFFD32F2F), fontSize = 12.sp, fontFamily = poppinsRegular)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (password.length >= 6 && password == confirmPassword && firstName.isNotBlank()) {
                        scope.launch {
                            try {
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user != null) {
                                    // 1. Update their password in Firebase Auth
                                    user.updatePassword(password).await()

                                    // 2. Save their details to Firestore
                                    val profileData = mapOf(
                                        "firstName" to firstName,
                                        "lastName" to lastName,
                                        "email" to email,
                                        "role" to role
                                    )
                                    repository.saveProfile(user.uid, profileData)

                                    // 3. Go to Dashboard
                                    navController.navigate("dashboard/$role/$firstName/$lastName") {
                                        popUpTo("get_started") { inclusive = true }
                                    }
                                }
                            } catch (e: Exception) {
                                errorMessage = e.message ?: "Failed to save profile."
                            }
                        }
                    } else {
                        errorMessage = "Please check your inputs."
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Complete Registration", fontSize = 18.sp, fontFamily = poppinsBold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextFieldWithValidation(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean,
    errorMessage: String,
    poppinsRegular: FontFamily
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = if (isError) Color(0xFF666666) else Color(0XFF757575),
                    fontSize = 14.sp,
                    fontFamily = poppinsRegular
                )
            },
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = poppinsRegular
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = if (isError) Color(0xFFFFEBEE) else Color.White,
                focusedContainerColor = if (isError) Color(0xFFFFEBEE) else Color.White,
                unfocusedBorderColor = if (isError) Color(0xFFD32F2F) else Color(0xFFA0A0A0),
                focusedBorderColor = if (isError) Color(0xFFD32F2F) else PrimaryGreen,
                cursorColor = PrimaryGreen,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                errorBorderColor = Color(0xFFD32F2F),
                errorContainerColor = Color(0xFFFFEBEE),
                focusedLabelColor = PrimaryGreen,
                unfocusedLabelColor = Color.Gray,
                errorTextColor = Color.Black,
                errorCursorColor = Color.Black
            ),
            visualTransformation = if (isPassword && !isPasswordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = {
                if (isPassword && onTogglePasswordVisibility != null) {
                    val icon = if (isPasswordVisible) {
                        painterResource(id = R.drawable.eye_solid_full)
                    } else {
                        painterResource(id = R.drawable.eye_slash_solid_full)
                    }

                    IconButton(
                        onClick = onTogglePasswordVisibility,
                        modifier = Modifier.size(25.dp)
                    ) {
                        Icon(
                            painter = icon,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            },
            isError = isError,
            singleLine = true,
            supportingText = {
                if (isError && errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFD32F2F),
                        fontSize = 11.sp,
                        fontFamily = poppinsRegular,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun AuthLabel(text: String, fontFamily: FontFamily) {
    Text(
        text = text,
        fontFamily = fontFamily,
        color = PrimaryGreen,
        fontSize = 17.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, start = 2.dp)
    )
}