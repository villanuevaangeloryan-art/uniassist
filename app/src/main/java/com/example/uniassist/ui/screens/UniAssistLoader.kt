package com.example.uniassist.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import kotlinx.coroutines.delay

@Composable
fun UniAssistLoader(onComplete: () -> Unit) {
    val brandGreen = Color(0xFF245B3D)

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
        val bobbing by rememberInfiniteTransition().animateFloat(
            initialValue = -10f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Image(
            painter = painterResource(id = R.drawable.gts_uniassist_logo),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .offset(y = bobbing.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

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

        Text(
            text = "Loading UNIASSIST...",
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            fontSize = 14.sp,
            color = brandGreen,
            modifier = Modifier.alpha(0.8f)
        )
    }
}