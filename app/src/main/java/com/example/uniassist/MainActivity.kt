package com.example.uniassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uniassist.data.AnnouncementRepository
import com.example.uniassist.data.TaskRepository
import com.example.uniassist.data.db.AppDatabase
import com.example.uniassist.ui.screens.*
import com.example.uniassist.ui.theme.UniassistTheme
import com.example.uniassist.ui.viewmodel.AnnouncementViewModel
import com.example.uniassist.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    // Initialize our local databases and repositories
    private val db by lazy { AppDatabase.getInstance(this) }

    // ViewModels for managing Announcements and Tasks via Room
    private val announcementViewModel: AnnouncementViewModel by viewModels {
        AnnouncementViewModel.Factory(AnnouncementRepository(db.announcementDao()))
    }

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModel.Factory(TaskRepository(db.taskDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the status bar look clean with our green theme
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            UniassistTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    val navController = rememberNavController()

                    // Temporary list for Teacher's Manage screen (UI state)
                    val publishedAnnouncements = remember { mutableStateListOf<ManageScreenAnnouncement>() }

                    NavHost(navController = navController, startDestination = "get_started") {

                        // --- ONBOARDING & AUTH ---

                        composable("get_started") {
                            GetStartedScreen(navController = navController)
                        }

                        // Still keeping RolePick for UI/UX flow, but login will strictly enforce the role
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


                        // --- MAIN DASHBOARDS (Strict Role Enforcement) ---

                        composable(
                            route = "dashboard/{role}/{firstName}/{lastName}",
                            arguments = listOf(
                                navArgument("role") { type = NavType.StringType },
                                navArgument("firstName") { type = NavType.StringType },
                                navArgument("lastName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
                            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
                            val fullName = "$firstName $lastName"

                            // If the backend says they are a Teacher, send them to Teacher Dashboard
                            if (role.equals("Teacher", ignoreCase = true)) {
                                TeacherDashboardScreen(
                                    userName = fullName,
                                    // Teacher specific logic
                                    onManageAnnouncements = { navController.navigate("manage_announcements") },
                                    onManageCampusInfo = { navController.navigate("manage_campus_department") },
                                    onSettings = { navController.navigate("settings/$role") },
                                    onLogout = {
                                        navController.navigate("get_started") { popUpTo(0) { inclusive = true } }
                                    }
                                )
                            } else {
                                // Otherwise, they get the Student Dashboard
                                DashboardScreen(
                                    role = role,
                                    firstName = firstName,
                                    lastName = lastName,
                                    announcementViewModel = announcementViewModel,
                                    onScheduleClick = { navController.navigate("schedule/$role") },
                                    onCampusClick = { navController.navigate("campus/$role") },
                                    onSettingsClick = { navController.navigate("settings/$role") }
                                )
                            }
                        }

                        // --- FEATURES ---

                        composable("manage_announcements") {
                            ManageAnnouncementsScreen(
                                onBackClick = { navController.popBackStack() },
                                onCreateClick = { navController.navigate("create_announcement") },
                                publishedAnnouncements = publishedAnnouncements
                            )
                        }

                        composable("create_announcement") {
                            CreateAnnouncementScreen(
                                onBackClick = { navController.popBackStack() },
                                onPublish = { newAnnouncement ->
                                    // Add to the local manage list for immediate UI feedback
                                    publishedAnnouncements.add(0, newAnnouncement.toManageCard(publishedAnnouncements.size + 1))

                                    // Persist it to the Room Database
                                    announcementViewModel.publish(
                                        announcement = Announcement(
                                            title = newAnnouncement.title,
                                            date = newAnnouncement.postDate,
                                            content = newAnnouncement.message,
                                            bullets = emptyList(),
                                            footer = if (newAnnouncement.isPriority) "⚠ Priority Announcement" else ""
                                        ),
                                        category = newAnnouncement.category,
                                        isPriority = newAnnouncement.isPriority
                                    )
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("schedule/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            ScheduleAndTaskScreen(
                                taskViewModel = taskViewModel,
                                onDashboardClick = { navController.popBackStack() },
                                onCampusClick = { navController.navigate("campus/$role") },
                                onSettingsClick = { navController.navigate("settings/$role") }
                            )
                        }

                        composable("campus/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            CampusInformationScreen(
                                onDashboardClick = { navController.popBackStack() },
                                onScheduleClick = { navController.navigate("schedule/$role") },
                                onSettingsClick = { navController.navigate("settings/$role") }
                            )
                        }

                        composable("manage_campus_department") {
                            ManageCampusDepartmentScreen(
                                onBackClick = { navController.popBackStack() },
                                onAddDepartmentClick = { navController.navigate("add_campus_department") },
                                onEditDepartmentClick = { index -> navController.navigate("edit_campus_department/$index") }
                            )
                        }

                        composable("settings/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            SettingsScreen(
                                onDashboardClick = { navController.popBackStack() },
                                onScheduleClick = { navController.navigate("schedule/$role") },
                                onCampusClick = { navController.navigate("campus/$role") },
                                onLogoutClick = {
                                    navController.navigate("get_started") { popUpTo(0) { inclusive = true } }
                                }
                            )
                        }

                        // NEW ROUTE for the second step
                        composable("complete_profile/{email}/{role}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            CompleteProfileScreen(navController, email, role)
                        }
                    }
                }
            }
        }
    }
}