package com.example.uniassist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uniassist.R
import com.example.uniassist.domain.actions.RegisterAction
import com.example.uniassist.ui.viewmodel.RegisterStore
import kotlinx.coroutines.delay

val PrimaryGreen = Color(0xFF245B3D)
val DarkGreenTint = Color(0xFF1B5E3C)
val FooterGray = Color(0xFF898989)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    role: String
) {
    val registerStore: RegisterStore = viewModel()
    val state = registerStore.state.collectAsState().value
    val focusManager = LocalFocusManager.current

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    var showLine by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showLine = true
        delay(400)
        showText = true
    }

    val poppinsBold = FontFamily(Font(R.font.poppins_bold))
    val poppinsMedium = FontFamily(Font(R.font.poppins_medium))
    val poppinsRegular = FontFamily(Font(R.font.poppins_regular))

    val modelImage = when (role) {
        "Teacher" -> R.drawable.uniassist_teacher
        else -> R.drawable.uniassist_student
    }

    LaunchedEffect(state.navigateToDashboard) {
        if (state.navigateToDashboard) {
            navController.navigate("dashboard/$role/${state.firstName}/${state.lastName}") {
                popUpTo("register") { inclusive = true }
            }
            registerStore.resetNavigation()
        }
    }

    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            navController.navigate("login_register/$role") {
                popUpTo("register") { inclusive = true }
            }
            registerStore.resetNavigation()
        }
    }

    if (state.isLoading) {
        UniAssistLoader {
            registerStore.dispatch(RegisterAction.LoadingComplete)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { registerStore.dispatch(RegisterAction.NavigateBack) },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                            .padding(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xFF1B5E3C))
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 100.dp)
                    ) {
                        AnimatedVisibility(
                            visible = showLine,
                            enter = expandVertically(tween(500), expandFrom = Alignment.Top)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(100.dp)
                                    .background(PrimaryGreen, RoundedCornerShape(2.dp))
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        AnimatedVisibility(
                            visible = showText,
                            enter = slideInHorizontally { -it / 2 } + fadeIn()
                        ) {
                            Column {
                                Text(
                                    "Join now !",
                                    fontSize = 31.sp,
                                    fontFamily = poppinsMedium,
                                    color = PrimaryGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Register and experience handy features\nof the app. Please register to continue.",
                                    fontSize = 11.sp,
                                    color = PrimaryGreen,
                                    lineHeight = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = poppinsMedium
                                )
                            }
                        }
                    }

                    Image(
                        painter = painterResource(id = modelImage),
                        contentDescription = "Role model image",
                        modifier = Modifier
                            .size(175.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = 30.dp, y = (-40).dp)
                    )
                }

                // Form Fields
                AuthLabel("First Name", poppinsBold)
                AuthTextFieldWithValidation(
                    value = state.firstName,
                    onValueChange = { registerStore.dispatch(RegisterAction.FirstNameChanged(it)) },
                    placeholder = "Enter first name",
                    isError = state.hasFirstNameError,
                    errorMessage = state.firstNameError,
                    poppinsRegular = poppinsRegular
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthLabel("Last Name", poppinsBold)
                AuthTextFieldWithValidation(
                    value = state.lastName,
                    onValueChange = { registerStore.dispatch(RegisterAction.LastNameChanged(it)) },
                    placeholder = "Enter last name",
                    isError = state.hasLastNameError,
                    errorMessage = state.lastNameError,
                    poppinsRegular = poppinsRegular
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthLabel("Email", poppinsBold)
                AuthTextFieldWithValidation(
                    value = state.email,
                    onValueChange = { registerStore.dispatch(RegisterAction.EmailChanged(it)) },
                    placeholder = "Enter your email",
                    keyboardType = KeyboardType.Email,
                    isError = state.hasEmailError,
                    errorMessage = state.emailError,
                    poppinsRegular = poppinsRegular
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthLabel("Username", poppinsBold)
                AuthTextFieldWithValidation(
                    value = state.username,
                    onValueChange = { registerStore.dispatch(RegisterAction.UsernameChanged(it)) },
                    placeholder = "Enter username",
                    isError = state.hasUsernameError,
                    errorMessage = state.usernameError,
                    poppinsRegular = poppinsRegular
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthLabel("Password", poppinsBold)
                AuthTextFieldWithValidation(
                    value = state.password,
                    onValueChange = { registerStore.dispatch(RegisterAction.PasswordChanged(it)) },
                    placeholder = "Enter password",
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                    isError = state.hasPasswordError,
                    errorMessage = state.passwordError,
                    poppinsRegular = poppinsRegular
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthLabel("Confirm Password", poppinsBold)
                AuthTextFieldWithValidation(
                    value = state.confirmPassword,
                    onValueChange = { registerStore.dispatch(RegisterAction.ConfirmPasswordChanged(it)) },
                    placeholder = "Confirm password",
                    isPassword = true,
                    isPasswordVisible = isConfirmPasswordVisible,
                    onTogglePasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                    isError = state.hasConfirmPasswordError,
                    errorMessage = state.confirmPasswordError,
                    poppinsRegular = poppinsRegular
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        registerStore.dispatch(RegisterAction.RegisterButtonClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White,
                        disabledContainerColor = PrimaryGreen.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    elevation = ButtonDefaults.buttonElevation(1.dp),
                    enabled = !state.isLoading
                ) {
                    Text("Register", fontSize = 19.sp, fontFamily = poppinsBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                val annotatedString = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(color = PrimaryGreen, fontFamily = poppinsBold)) {
                        append("Log in here.")
                    }
                }
                Text(
                    text = annotatedString,
                    fontSize = 11.sp,
                    color = PrimaryGreen,
                    fontFamily = poppinsMedium,
                    modifier = Modifier.clickable {
                        navController.navigate("login/$role") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    "By TechSpace",
                    fontSize = 13.sp,
                    fontFamily = poppinsBold,
                    color = FooterGray,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
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
        color = Color(0XFF245B3D),
        fontSize = 17.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, start = 2.dp)
    )
}