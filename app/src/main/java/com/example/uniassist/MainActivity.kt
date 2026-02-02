package com.example.uniassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uniassist.ui.screens.CampusInformationScreen
import com.example.uniassist.ui.theme.UniassistTheme
import com.example.uniassist.ui.screens.GetStartedScreen
import com.example.uniassist.ui.screens.LoginOrRegisterScreen
import com.example.uniassist.ui.screens.LoginScreen
import com.example.uniassist.ui.screens.RegisterScreen
import com.example.uniassist.ui.screens.ScheduleAndTaskScreen
import com.example.uniassist.ui.screens.SettingsScreen
import androidx.core.view.WindowCompat
class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

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

                    composable("campus") {
                        CampusInformationScreen(
                            onDashboardClick = {
                                navController.navigate("dashboard") { launchSingleTop = true }
                            },
                            onScheduleClick = {
                                navController.navigate("schedule") { launchSingleTop = true }
                            },
                            onSettingsClick = {
                                navController.navigate("settings") { launchSingleTop = true }
                            }
                        )
                    }

                    composable("settings") {
                        SettingsScreen(
                            onDashboardClick = {
                                navController.navigate("dashboard") { launchSingleTop = true }
                            },
                            onScheduleClick = {
                                navController.navigate("schedule") { launchSingleTop = true }
                            },
                            onCampusClick =  {
                                navController.navigate("campus") { launchSingleTop = true }
                            }
                        )
                    }


                }
            }
        }
    }
}

