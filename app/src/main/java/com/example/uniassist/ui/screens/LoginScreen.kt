
package com.example.uniassist.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    role: String
) {
    val loginStore: LoginStore = viewModel()
    val state = loginStore.state.collectAsState().value
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // --- Google Sign-In setup ---
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    loginStore.dispatch(LoginAction.GoogleSignInClicked(idToken))
                }
            } catch (e: ApiException) { }
        }
    }

    val modelImage = when (role) {
        "Teacher" -> R.drawable.uniassist_teacher
        else -> R.drawable.uniassist_student
    }

    var showLine by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showLine = true
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
                        .verticalScroll(scrollState)
                        .imePadding() // Ensures the UI moves up when the keyboard is open
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { loginStore.dispatch(LoginAction.NavigateBack) },
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                                .padding(4.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xFF1B5E3C))
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

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
                                .size(280.dp)
                                .rotate(-14f)
                                .align(Alignment.CenterEnd)
                                .offset(x = 140.dp, y = 0.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Email Field
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
                        placeholder = { Text("Enter email address", fontSize = 14.sp, color = Color(0XFF757575)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = if (state.hasEmailError) Color(0xFFD32F2F) else Color(0xFF1B5E3C),
                            unfocusedBorderColor = if (state.hasEmailError) Color(0xFFD32F2F) else Color.LightGray
                        ),
                        isError = state.hasEmailError,
                        singleLine = true,
                        supportingText = {
                            if (state.hasEmailError) {
                                Text(state.emailError, color = Color(0xFFD32F2F), fontSize = 11.sp)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
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
                        placeholder = { Text("Enter password", fontSize = 14.sp, color = Color(0XFF757575)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = if (state.hasPasswordError) Color(0xFFD32F2F) else Color(0xFF1B5E3C),
                            unfocusedBorderColor = if (state.hasPasswordError) Color(0xFFD32F2F) else Color.LightGray
                        ),
                        trailingIcon = {
                            val icon = if (state.isPasswordVisible) R.drawable.eye_solid_full else R.drawable.eye_slash_solid_full
                            IconButton(onClick = { loginStore.dispatch(LoginAction.TogglePasswordVisibility) }) {
                                Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color(0xFF1B5E3C), modifier = Modifier.size(24.dp))
                            }
                        },
                        isError = state.hasPasswordError,
                        singleLine = true,
                        supportingText = {
                            if (state.hasPasswordError) {
                                Text(state.passwordError, color = Color(0xFFD32F2F), fontSize = 11.sp)
                            }
                        }
                    )

                    ErrorBanner(errorMessage = state.authError)

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            loginStore.dispatch(LoginAction.LoginButtonClicked(role))
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E3C)),
                        shape = RoundedCornerShape(50),
                        enabled = state.isFormValid
                    ) {
                        Text("Log in", fontFamily = FontFamily(Font(R.font.poppins_bold)), fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                        Text("  or  ", fontSize = 12.sp, color = Color.Gray)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            googleSignInClient.signOut().addOnCompleteListener {
                                googleSignInLauncher.launch(googleSignInClient.signInIntent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.LightGray),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Image(painter = painterResource(id = R.drawable.googlelogo), contentDescription = "Google", modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Continue with Google", fontSize = 15.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Don't have an account yet? ", fontSize = 12.sp, color = Color.Gray)
                        TextButton(onClick = { loginStore.dispatch(LoginAction.NavigateToRegister) }) {
                            Text("Register here", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E3C))
                        }
                    }

                    Text(
                        "By TechSpace",
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorBanner(errorMessage: String) {
    if (errorMessage.isEmpty()) return
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDECEA)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFD32F2F).copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(errorMessage, color = Color(0xFFD32F2F), fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}
