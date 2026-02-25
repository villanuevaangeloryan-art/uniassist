package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

@Composable
fun GetStartedScreen(navController: NavController) {
    LandingPageContent {
        navController.navigate("role_pick") {
            popUpTo("get_started") { inclusive = true }
        }
    }
}

@Composable
fun LandingPageContent(onGetStarted: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.gts_bg_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDEF8CB).copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.gts_uniassist_logo),
                contentDescription = "UniAssist Logo",
                modifier = Modifier.scale(0.95f)
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = onGetStarted,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.size(290.dp, 54.dp),
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