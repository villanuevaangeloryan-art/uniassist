package com.example.uniassist.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uniassist.R
import com.example.uniassist.data.FirebaseAuthRepository
import com.example.uniassist.ui.theme.BottomNavGreen
import com.example.uniassist.ui.theme.GreyText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun LoginOrRegisterScreen(
    navController: NavController,
    role: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { FirebaseAuthRepository() }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    scope.launch {
                        try {
                            val email = repository.signInWithGoogle(idToken)
                            val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

                            if (currentUser != null) {
                                val alreadyRegistered = repository.isUserRegistered(currentUser.uid)

                                if (alreadyRegistered) {
                                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                                    errorMessage = "This email is already registered. Please use Login."
                                } else {
                                    errorMessage = ""
                                    navController.navigate("complete_profile/$email/$role")
                                }
                            }
                        } catch (e: Exception) {
                            errorMessage = "Authentication failed."
                        }
                    }
                }
            } catch (e: ApiException) {
                errorMessage = "Google sign-in failed."
            }
        }
    }

    val poppinsBold = FontFamily(Font(R.font.poppins_bold))
    val poppinsMedium = FontFamily(Font(R.font.poppins_medium))

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_rolepick),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .background(Color.White, shape = CircleShape)
                        .size(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = BottomNavGreen)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.gts_uniassist_logo),
                contentDescription = null,
                modifier = Modifier.scale(0.95f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        fontFamily = poppinsMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = { navController.navigate("login/$role") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BottomNavGreen),
                    shape = RoundedCornerShape(50.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Login", fontSize = 18.sp, fontFamily = poppinsBold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BottomNavGreen.copy(alpha = 0.5f))
                    Text(
                        text = "or",
                        color = BottomNavGreen.copy(alpha = 0.7f),
                        fontFamily = poppinsMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = BottomNavGreen.copy(alpha = 0.5f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = {
                        googleSignInClient.signOut().addOnCompleteListener {
                            googleAuthLauncher.launch(googleSignInClient.signInIntent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    border = BorderStroke(2.dp, BottomNavGreen),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.googlelogo),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Register with Google", fontSize = 18.sp, fontFamily = poppinsBold, color = BottomNavGreen)
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "By TechSpace",
                    fontSize = 13.sp,
                    fontFamily = poppinsBold,
                    color = GreyText,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
    }
}