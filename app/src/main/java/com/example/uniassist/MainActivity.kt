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
import com.example.uniassist.ui.screens.RolePickScreen
import androidx.core.view.WindowCompat
import com.example.uniassist.ui.screens.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            UniassistTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "get_started"
                ) {
                    composable("get_started") {
                        GetStartedScreen(navController = navController)
                    }

                    composable("role_pick") {
                        RolePickScreen(navController = navController)
                    }

                    composable("login_register/{role}") { backStackEntry ->
                        val role = backStackEntry.arguments?.getString("role") ?: "Student"
                        LoginOrRegisterScreen(
                            navController = navController,
                            role = role
                        )
                    }

                    composable("login/{role}") { backStackEntry ->
                        val role = backStackEntry.arguments?.getString("role") ?: "Student"
                        LoginScreen(
                            navController = navController,
                            role = role
                        )
                    }

                    composable("register/{role}") { backStackEntry ->
                        val role = backStackEntry.arguments?.getString("role") ?: "Student"
                        RegisterScreen(
                            navController = navController,
                            role = role
                        )
                    }

                    // Dashboard with role
                    composable("dashboard/{role}") { backStackEntry ->
                        val role = backStackEntry.arguments?.getString("role") ?: "Student"
                        DashboardScreen(
                            role = role,
                            onScheduleClick = {
                                navController.navigate("schedule/${role}") {
                                    launchSingleTop = true
                                }
                            },
                            onCampusClick = {
                                navController.navigate("campus/${role}") {
                                    launchSingleTop = true
                                }
                            },
                            onSettingsClick = {
                                navController.navigate("settings/${role}") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    // Schedule with role
                    composable("schedule/{role}") { backStackEntry ->
                        val role = backStackEntry.arguments?.getString("role") ?: "Student"
                        ScheduleAndTaskScreen(
                            onDashboardClick = {
                                navController.navigate("dashboard/${role}") {
                                    popUpTo("dashboard/${role}") { inclusive = true }
                                }
                            },
                            onCampusClick = {
                                navController.navigate("campus/${role}")
                            },
                            onSettingsClick = {
                                navController.navigate("settings/${role}")
                            }
                        )
                    }

                    // Campus with role
                    composable("campus/{role}") { backStackEntry ->
                        val role = backStackEntry.arguments?.getString("role") ?: "Student"
                        CampusInformationScreen(
                            onDashboardClick = {
                                navController.navigate("dashboard/${role}") {
                                    popUpTo("dashboard/${role}") { inclusive = true }
                                }
                            },
                            onScheduleClick = {
                                navController.navigate("schedule/${role}")
                            },
                            onSettingsClick = {
                                navController.navigate("settings/${role}")
                            }
                        )
                    }

                    // Settings with role
                    composable("settings/{role}") { backStackEntry ->
                        val role = backStackEntry.arguments?.getString("role") ?: "Student"
                        SettingsScreen(
                            onDashboardClick = {
                                navController.navigate("dashboard/${role}") {
                                    popUpTo("dashboard/${role}") { inclusive = true }
                                }
                            },
                            onScheduleClick = {
                                navController.navigate("schedule/${role}")
                            },
                            onCampusClick = {
                                navController.navigate("campus/${role}")
                            },
                            onLogoutClick = {
                                navController.navigate("login_register/Student") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}