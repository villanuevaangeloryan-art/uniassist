package com.example.uniassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uniassist.ui.screens.CampusInformationScreen
import com.example.uniassist.ui.theme.UniassistTheme
import com.example.uniassist.ui.screens.LoginOrRegisterScreen
import com.example.uniassist.ui.screens.LoginScreen
import com.example.uniassist.ui.screens.RegisterScreen
import com.example.uniassist.ui.screens.ScheduleAndTaskScreen
import com.example.uniassist.ui.screens.SettingsScreen
import com.example.uniassist.ui.screens.RolePickScreen
import com.example.uniassist.ui.screens.DashboardScreen
import com.example.uniassist.ui.screens.TeacherDashboardScreen
import com.example.uniassist.ui.screens.GetStartedScreen
import com.example.uniassist.ui.screens.ManageAnnouncementsScreen
import com.example.uniassist.ui.screens.CreateAnnouncementScreen
import com.example.uniassist.ui.screens.ManageCampusDepartmentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            UniassistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
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
                            LoginOrRegisterScreen(navController = navController, role = role)
                        }

                        composable("login/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            LoginScreen(navController = navController, role = role)
                        }

                        composable("register/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            RegisterScreen(navController = navController, role = role)
                        }

                        composable("dashboard/{role}/{firstName}/{lastName}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
                            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
                            val fullName = if (firstName.isNotBlank() && lastName.isNotBlank()) {
                                "$firstName $lastName"
                            } else {
                                if (role == "Teacher") "Prof. Angelo Villanueva" else "Angelo\nVillanueva"
                            }

                            if (role == "Teacher") {
                                TeacherDashboardScreen(
                                    userName = fullName,
                                    totalStudents = 128,
                                    totalAnnouncements = 23,
                                    totalUnread = 5,
                                    onManageAnnouncements = {
                                        navController.navigate("manage_announcements")
                                    },
                                    onManageCampusInfo = {
                                        navController.navigate("manage_campus_department")
                                    },
                                    onViewStudents = {},
                                    onSettings = {
                                        navController.navigate("settings/${role}")
                                    },
                                    onDrafts = {},
                                    onArchive = {},
                                    onLogout = {
                                        navController.navigate("get_started") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            } else {
                                DashboardScreen(
                                    role = role,
                                    firstName = firstName,
                                    lastName = lastName,
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
                        }

                        composable("manage_announcements") {
                            ManageAnnouncementsScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onCreateClick = {
                                    navController.navigate("create_announcement")
                                }
                            )
                        }

                        composable("manage_campus_department") {
                            ManageCampusDepartmentScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("create_announcement") {
                            CreateAnnouncementScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("schedule/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            ScheduleAndTaskScreen(
                                onDashboardClick = {
                                    navController.navigate("dashboard/${role}//") {
                                        popUpTo("dashboard/${role}//") { inclusive = true }
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

                        composable("campus/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            CampusInformationScreen(
                                onDashboardClick = {
                                    navController.navigate("dashboard/${role}//") {
                                        popUpTo("dashboard/${role}//") { inclusive = true }
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

                        composable("settings/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            SettingsScreen(
                                onDashboardClick = {
                                    navController.navigate("dashboard/${role}//") {
                                        popUpTo("dashboard/${role}//") { inclusive = true }
                                    }
                                },
                                onScheduleClick = {
                                    navController.navigate("schedule/${role}")
                                },
                                onCampusClick = {
                                    navController.navigate("campus/${role}")
                                },
                                onLogoutClick = {
                                    navController.navigate("get_started") {
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
}