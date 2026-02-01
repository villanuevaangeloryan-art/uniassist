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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    // Get the LoginStore (ViewModel)
    val loginStore: LoginStore = viewModel()
    val state = loginStore.state.collectAsState().value
    val focusManager = LocalFocusManager.current

    // Animation states (local to UI - animations don't need to be in state)
    var showLine by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showLine = true
        delay(400)
        showText = true
    }

    // Handle navigation based on state
    LaunchedEffect(state.navigateToHome) {
        if (state.navigateToHome) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
                loginStore.resetNavigation()
            }
        }
    }

    LaunchedEffect(state.navigateToRegister) {
        if (state.navigateToRegister) {
            navController.navigate("register")
            loginStore.resetNavigation()
        }
    }

    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            navController.popBackStack()
            loginStore.resetNavigation()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFDEF8CB).copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        loginStore.dispatch(LoginAction.NavigateBack)
                    },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1B5E3C)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.gts_uniassist_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .alpha(0.5f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Welcome Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = showLine,
                        enter = expandVertically(
                            animationSpec = tween(500),
                            expandFrom = Alignment.Top
                        )
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
                        enter = slideInHorizontally(
                            initialOffsetX = { -it / 2 }
                        ) + fadeIn()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = "Welcome back!",
                                fontSize = 28.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_extrabold)),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E3C)
                            )

                            Text(
                                text = "We're glad to see you again.\nPlease log in to continue.",
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.model_uniassist),
                    contentDescription = "Brand image",
                    modifier = Modifier
                        .size(400.dp)
                        .rotate(-14f)
                        .align(Alignment.CenterEnd)
                        .offset(x = 166.dp, y = 0.dp)
                )
            }

            // Email Field
            Column(
                modifier = Modifier.height(112.dp)
            ) {
                Text(
                    text = "Email",
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B5E3C)
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { newEmail ->
                        loginStore.dispatch(LoginAction.EmailChanged(newEmail))
                    },
                    placeholder = {
                        Text(
                            "Enter email address",
                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
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
                            Text(
                                text = state.emailError,
                                color = Color(0xFFD32F2F),
                                fontSize = 11.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        }
                    }
                )
            }

            // Password Field
            Column(
                modifier = Modifier.height(118.dp)
            ) {
                Text(
                    text = "Password",
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B5E3C)
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { newPassword ->
                        loginStore.dispatch(LoginAction.PasswordChanged(newPassword))
                    },
                    placeholder = {
                        Text(
                            "Enter password",
                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (state.isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
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
                        val icon = if (state.isPasswordVisible) {
                            painterResource(id = R.drawable.eye_solid_full)
                        } else {
                            painterResource(id = R.drawable.eye_slash_solid_full)
                        }

                        IconButton(
                            onClick = {
                                loginStore.dispatch(LoginAction.TogglePasswordVisibility)
                            },
                            modifier = Modifier.size(25.dp)
                        ) {
                            Icon(
                                painter = icon,
                                contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password",
                                tint = Color(0xFF1B5E3C),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    },
                    isError = state.hasPasswordError,
                    singleLine = true,
                    supportingText = {
                        if (state.hasPasswordError) {
                            Text(
                                text = state.passwordError,
                                color = Color(0xFFD32F2F),
                                fontSize = 11.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                maxLines = 2
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Login Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    loginStore.dispatch(LoginAction.LoginButtonClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B5E3C),
                    disabledContainerColor = Color(0xFF1B5E3C).copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp,
                    disabledElevation = 0.dp
                ),
                enabled = !state.isLoading && state.isFormValid
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        "Log in",
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            // Registration link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account yet? ",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    color = Color.Gray
                )

                TextButton(
                    onClick = {
                        loginStore.dispatch(LoginAction.NavigateToRegister)
                    },
                    modifier = Modifier.padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                ) {
                    Text(
                        text = "Register here",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        color = Color(0xFF1B5E3C)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 35.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "By TechSpace",
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 13.sp,
                    color = Color(0xFF898989)
                )
            }
        }
    }
}