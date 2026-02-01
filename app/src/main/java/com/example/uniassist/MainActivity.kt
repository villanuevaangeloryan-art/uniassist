package com.example.uniassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uniassist.ui.theme.UniassistTheme
import com.example.uniassist.ui.screens.GetStartedScreen
import com.example.uniassist.ui.screens.LoginOrRegisterScreen
import com.example.uniassist.ui.screens.LoginScreen
import com.example.uniassist.ui.screens.RegisterScreen
import com.example.uniassist.ui.screens.ScheduleAndTaskScreen
class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            UniassistTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "get_started"
                ){
                    composable("get_started"){
                        GetStartedScreen(navController)
                    }

                    composable("login_register"){
                        LoginOrRegisterScreen(navController)
                    }

                    composable("login"){
                        LoginScreen(navController)
                    }

                    composable("register"){
                        RegisterScreen(navController)
                    }

                    composable("dashboard") {
                        DashboardScreen(
                            onScheduleClick = {
                                navController.navigate("schedule") { launchSingleTop = true }
                            },
                            onCampusClick = {
                                navController.navigate("campus") { launchSingleTop = true }
                            },
                            onSettingsClick = {
                                navController.navigate("settings") { launchSingleTop = true }
                            }
                        )
                    }

                    composable("schedule") {
                        ScheduleAndTaskScreen(
                            onDashboardClick = {
                                navController.navigate("dashboard") { launchSingleTop = true }
                            },
                            onCampusClick = {
                                navController.navigate("campus") { launchSingleTop = true }
                            },
                            onSettingsClick = {
                                navController.navigate("settings") { launchSingleTop = true }
                            }
                        )
                    }


                }
            }
        }
    }
}

