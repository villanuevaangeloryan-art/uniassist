package com.example.uniassist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uniassist.R
import kotlinx.coroutines.delay

@Composable
fun GetStartedScreen(navController: NavController) {
    var isStarting by remember { mutableStateOf(false) }

    // Use AnimatedContent for a more sophisticated transition than Crossfade
    AnimatedContent(
        targetState = isStarting,
        transitionSpec = {
            fadeIn(animationSpec = tween(600)) togetherWith
                    fadeOut(animationSpec = tween(600))
        },
        label = "screen_switch"
    ) { loading ->
        if (loading) {
            UniAssistLoader {
                navController.navigate("login_register") {
                    popUpTo("get_started") { inclusive = true }
                }
            }
        } else {
            LandingPageContent(onGetStarted = { isStarting = true })
        }
    }
}

@Composable
fun LandingPageContent(onGetStarted: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.gts_bg_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.4f),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.fillMaxSize().background(Color(0xfFDEF8CB).copy(alpha = 0.5f)))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Animated Entrance for Logo
            val logoAlpha by animateFloatAsState(targetValue = 1f, animationSpec = tween(1000), label = "")

            Image(
                painter = painterResource(id = R.drawable.gts_uniassist_logo),
                contentDescription = "UniAssist Logo",
                modifier = Modifier.alpha(logoAlpha).scale(0.95f)
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = onGetStarted,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.size(290.dp, 54.dp), // Slightly taller for better touch target
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF245B3D)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)
            ) {
                Text(
                    "Get Started",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = "By TechSpace",
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 13.sp,
                color = Color(0xFF898989),
                modifier = Modifier.padding(bottom = 35.dp)
            )
        }
    }
}

@Composable
fun UniAssistLoader(onComplete: () -> Unit) {
    val brandGreen = Color(0xFF245B3D)

    // Animation states
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2500, easing = LinearOutSlowInEasing)
        )
        delay(200)
        onComplete()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Floating/Bobbing animation for the pen logo
        val bobbing by rememberInfiniteTransition(label = "").animateFloat(
            initialValue = -10f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        Image(
            painter = painterResource(id = R.drawable.gts_uniassist_logo),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .offset(y = bobbing.dp) // Pen "floats" while writing
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Creative Progress Bar (The "Ink Stroke")
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(brandGreen.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.value)
                    .background(brandGreen)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dynamic Loading Text
        val loadingText = "Loading UNIASSIST..."

        Text(
            text = loadingText,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            fontSize = 14.sp,
            color = brandGreen,
            modifier = Modifier.alpha(0.8f)
        )
    }
}