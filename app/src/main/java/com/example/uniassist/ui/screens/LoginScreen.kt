package com.example.uniassist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniassist.R
import com.example.uniassist.domain.actions.LoginAction
import com.example.uniassist.ui.viewmodel.LoginStore
import kotlinx.coroutines.delay
import com.example.uniassist.ui.screens.UniAssistLoader   // ← ADD THIS IMPORT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    role: String
) {
    val loginStore: LoginStore = viewModel()
    val state = loginStore.state.collectAsState().value
    val focusManager = LocalFocusManager.current

    val modelImage = when (role) {
        "Teacher" -> R.drawable.uniassist_teacher
        else -> R.drawable.uniassist_student
    }

    var showLine by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showLine = true
        delay(400)
        showText = true
    }

    LaunchedEffect(state.navigateToDashboard) {
        if (state.navigateToDashboard) {
            navController.navigate("dashboard/$role/${state.firstName}/${state.lastName}") {
                popUpTo(0) { inclusive = true }
            }
            loginStore.resetNavigation()
        }
    }

    LaunchedEffect(state.navigateToRegister) {
        if (state.navigateToRegister) {
            navController.navigate("register/$role")
            loginStore.resetNavigation()
        }
    }

    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            navController.navigate("login_register/$role") {
                popUpTo("login") { inclusive = true }
            }
            loginStore.resetNavigation()
        }
    }

    // Show loader when isLoading is true
    if (state.isLoading) {
        UniAssistLoader {
            loginStore.dispatch(LoginAction.LoadingComplete)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier.fillMaxSize().statusBarsPadding(),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { loginStore.dispatch(LoginAction.NavigateBack) },
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                                .padding(8.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xFF1B5E3C))
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            AnimatedVisibility(
                                visible = showLine,
                                enter = expandVertically(tween(500), expandFrom = Alignment.Top)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(100.dp)
                                        .background(Color(0xFF1B5E3C))
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            AnimatedVisibility(
                                visible = showText,
                                enter = slideInHorizontally { -it / 2 } + fadeIn()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        "Welcome back!",
                                        fontSize = 31.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E3C)
                                    )
                                    Text(
                                        "We're glad to see you again.\nPlease log in to continue.",
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        fontSize = 11.sp,
                                        color = Color(0XFF245B3D),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                        Image(
                            painter = painterResource(id = modelImage),
                            contentDescription = null,
                            modifier = Modifier
                                .size(300.dp)
                                .rotate(-14f)
                                .align(Alignment.CenterEnd)
                                .offset(x = 153.dp, y = 0.dp)
                        )
                    }

                    Column(modifier = Modifier.height(110.dp)) {
                        Text(
                            "Email",
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B5E3C)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = { loginStore.dispatch(LoginAction.EmailChanged(it)) },
                            placeholder = { Text("Enter email address", fontFamily = FontFamily(Font(R.font.poppins_regular)), fontSize = 14.sp, color = Color(0XFF757575)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_regular)), fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = if (state.hasEmailError) Color(0xFFD32F2F) else Color(0xFF1B5E3C),
                                unfocusedBorderColor = if (state.hasEmailError) Color(0xFFD32F2F) else Color.LightGray,
                                errorBorderColor = Color(0xFFD32F2F),
                                errorContainerColor = Color(0xFFFFEBEE),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                errorTextColor = Color(0xFFD32F2F),
                                errorLabelColor = Color(0xFFD32F2F)
                            ),
                            isError = state.hasEmailError,
                            singleLine = true,
                            supportingText = {
                                if (state.hasEmailError) {
                                    Text(state.emailError, color = Color(0xFFD32F2F), fontSize = 11.sp, fontFamily = FontFamily(Font(R.font.poppins_regular)))
                                }
                            }
                        )
                    }

                    Column(modifier = Modifier.height(125.dp)) {
                        Text(
                            "Password",
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B5E3C)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = state.password,
                            onValueChange = { loginStore.dispatch(LoginAction.PasswordChanged(it)) },
                            placeholder = { Text("Enter password", fontFamily = FontFamily(Font(R.font.poppins_regular)), fontSize = 14.sp, color = Color(0XFF757575)) },
                            textStyle = TextStyle(fontFamily = FontFamily(Font(R.font.poppins_regular)), fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = if (state.hasPasswordError) Color(0xFFD32F2F) else Color(0xFF1B5E3C),
                                unfocusedBorderColor = if (state.hasPasswordError) Color(0xFFD32F2F) else Color.LightGray,
                                errorBorderColor = Color(0xFFD32F2F),
                                errorContainerColor = Color(0xFFFFEBEE),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                errorTextColor = Color(0xFFD32F2F),
                                errorLabelColor = Color(0xFFD32F2F)
                            ),
                            trailingIcon = {
                                val icon = if (state.isPasswordVisible) R.drawable.eye_solid_full else R.drawable.eye_slash_solid_full
                                IconButton(
                                    onClick = { loginStore.dispatch(LoginAction.TogglePasswordVisibility) },
                                    modifier = Modifier.size(25.dp)
                                ) {
                                    Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color(0xFF1B5E3C))
                                }
                            },
                            isError = state.hasPasswordError,
                            singleLine = true,
                            supportingText = {
                                if (state.hasPasswordError) {
                                    Text(state.passwordError, color = Color(0xFFD32F2F), fontSize = 11.sp, fontFamily = FontFamily(Font(R.font.poppins_regular)), maxLines = 2)
                                }
                            }
                        )
                    }

                    // --- AUTH ERROR DISPLAY ---
                    if (state.authError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = state.authError,
                            color = Color(0xFFD32F2F),
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    } else {
                        Spacer(modifier = Modifier.height(75.dp))
                    }

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            // Pass the 'role' variable down into the action!
                            loginStore.dispatch(LoginAction.LoginButtonClicked(role))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1B5E3C),
                            disabledContainerColor = Color(0xFF1B5E3C).copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(50),
                        enabled = !state.isLoading && state.isFormValid
                    ) {
                        Text("Log in", fontFamily = FontFamily(Font(R.font.poppins_bold)), fontSize = 20.sp, color = Color.White)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Don't have an account yet? ", fontSize = 11.sp, fontFamily = FontFamily(Font(R.font.poppins_medium)), color = Color.Gray)
                        TextButton(onClick = { loginStore.dispatch(LoginAction.NavigateToRegister) }) {
                            Text("Register here", fontSize = 11.sp, fontFamily = FontFamily(Font(R.font.poppins_bold)), color = Color(0xFF1B5E3C))
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.5f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 35.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("By TechSpace", fontFamily = FontFamily(Font(R.font.poppins_bold)), fontSize = 13.sp, color = Color(0xFF898989))
                    }
                }
            }
        }
    }
}