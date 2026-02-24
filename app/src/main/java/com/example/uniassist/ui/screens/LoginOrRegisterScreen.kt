package com.example.uniassist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
fun LoginOrRegisterScreen (navController: NavController, modifier: Modifier = Modifier){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ){
        Image(
            contentDescription = "Background Image",
            painter = painterResource(id = R.drawable.gts_bg_background),
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFDEF8CB).copy(alpha = 0.5f))
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = {
                    navController.navigate("role_pick")
                },
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                    .padding(8.dp)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1B5E3C),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            // Main content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    contentDescription = "UniAssist Logo",
                    painter = painterResource(id = R.drawable.gts_uniassist_logo),
                )

                Spacer(modifier = Modifier.height(40.dp))

                //Log in button
                Button(
                    onClick = {
                        navController.navigate("login")
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .size(290.dp, 49.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF245B3D),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(1.dp)
                ){
                    Text(
                        "Login",
                        fontSize = 19.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(modifier = Modifier.width(88.dp))
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = Color(0xDA245B3D).copy(alpha = 0.5f)
                    )

                    Text(
                        "or",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        color = Color(0xDA245B3D).copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = Color(0xDA245B3D).copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(88.dp))
                }

                Spacer(modifier = Modifier.height(3.dp))

                //Register button
                Button(
                    onClick = {
                        navController.navigate("register")
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .size(290.dp, 49.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF245B3D)
                    ),
                    border = BorderStroke(3.dp, Color(0xff245B3D))
                ){
                    Text(
                        "Register",
                        fontSize = 19.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
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