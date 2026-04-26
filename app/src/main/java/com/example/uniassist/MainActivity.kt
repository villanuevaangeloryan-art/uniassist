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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uniassist.data.AnnouncementRepository
import com.example.uniassist.data.FirestoreStudentRepository
import com.example.uniassist.data.TaskRepository
import com.example.uniassist.data.db.AppDatabase
import com.example.uniassist.data.db.CampusRepository
import com.example.uniassist.ui.screens.*
import com.example.uniassist.ui.theme.UniassistTheme
import com.example.uniassist.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    // DB → DAO → Repository → ViewModel
    private val db by lazy { AppDatabase.getInstance(this) }

    private val announcementViewModel: AnnouncementViewModel by viewModels {
        AnnouncementViewModel.Factory(AnnouncementRepository(db.announcementDao()))
    }

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModel.Factory(TaskRepository(db.taskDao()))
    }

    private val campusViewModel: CampusViewModel by viewModels {
        CampusViewModelFactory(CampusRepository(db.campusDao()))
    }

    private val studentViewModel: StudentViewModel by viewModels {
        StudentViewModelFactory(FirestoreStudentRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the status bar look clean with our theme
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            UniassistTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    val navController = rememberNavController()

                    // Teacher's manage-screen list (UI only, not persisted).
                    val publishedAnnouncements = remember { mutableStateListOf<ManageScreenAnnouncement>() }

                    NavHost(navController = navController, startDestination = "get_started") {

                        // --- AUTHENTICATION & ONBOARDING ---

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

                        composable("complete_profile/{email}/{role}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            CompleteProfileScreen(navController, email, role)
                        }

                        // --- DASHBOARDS ---

                        composable("dashboard/{role}/{firstName}/{lastName}") { backStackEntry ->
                            val role      = backStackEntry.arguments?.getString("role")      ?: "Student"
                            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
                            val lastName  = backStackEntry.arguments?.getString("lastName")  ?: ""
                            val fullName  = if (firstName.isNotBlank() && lastName.isNotBlank()) {
                                "$firstName $lastName"
                            } else {
                                if (role == "Teacher") "Prof. Angelo Villanueva" else "Angelo\nVillanueva"
                            }

                            if (role == "Teacher") {
                                TeacherDashboardScreen(
                                    userName           = fullName,
                                    totalStudents      = 128,
                                    totalAnnouncements = 23,
                                    totalUnread        = 5,
                                    onManageAnnouncements = { navController.navigate("manage_announcements") },
                                    onManageCampusInfo    = { navController.navigate("manage_campus_department") },
                                    onViewStudents        = { navController.navigate("view_students") },
                                    onSettings            = { navController.navigate("settings/${role}") },
                                    onDrafts              = {},
                                    onArchive             = {},
                                    onLogout = {
                                        navController.navigate("get_started") { popUpTo(0) { inclusive = true } }
                                    }
                                )
                            } else {
                                // Student dashboard receives the shared ViewModel.
                                DashboardScreen(
                                    role                 = role,
                                    firstName            = firstName,
                                    lastName             = lastName,
                                    announcementViewModel = announcementViewModel,
                                    onScheduleClick = {
                                        navController.navigate("schedule/${role}") { launchSingleTop = true }
                                    },
                                    onCampusClick = {
                                        navController.navigate("campus/${role}") { launchSingleTop = true }
                                    },
                                    onSettingsClick = {
                                        navController.navigate("settings/${role}") { launchSingleTop = true }
                                    }
                                )
                            }
                        }

                        // --- TEACHER FEATURES ---

                        composable("view_students") {
                            StudentListScreen(
                                viewModel = studentViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable("manage_announcements") {
                            ManageAnnouncementsScreen(
                                onBackClick  = { navController.popBackStack() },
                                onCreateClick = { navController.navigate("create_announcement") },
                                publishedAnnouncements = publishedAnnouncements
                            )
                        }

                        composable("create_announcement") {
                            CreateAnnouncementScreen(
                                onBackClick  = { navController.popBackStack() },
                                onSaveDraft  = { navController.popBackStack() },
                                onPublish = { newAnnouncement ->

                                    publishedAnnouncements.add(
                                        0,
                                        newAnnouncement.toManageCard(id = publishedAnnouncements.size + 100)
                                    )

                                    announcementViewModel.publish(
                                        announcement = Announcement(
                                            title   = newAnnouncement.title,
                                            date    = newAnnouncement.postDate,
                                            content = newAnnouncement.message,
                                            bullets = emptyList(),
                                            footer  = if (newAnnouncement.isPriority) "⚠ Priority Announcement" else ""
                                        ),
                                        category   = newAnnouncement.category,
                                        isPriority = newAnnouncement.isPriority
                                    )

                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("manage_campus_department") {
                            ManageCampusDepartmentScreen(
                                viewModel = campusViewModel,
                                onBackClick          = { navController.popBackStack() },
                                onAddDepartmentClick = { navController.navigate("add_campus_department") },
                                onEditDepartmentClick = { index ->
                                    navController.navigate("edit_campus_department/$index")
                                }
                            )
                        }

                        composable("add_campus_department") {
                            AddCampusDepartmentScreen(
                                viewModel    = campusViewModel,
                                onBackClick  = { navController.popBackStack() },
                                onSaveDraft  = { navController.popBackStack() },
                                onPublish    = { navController.popBackStack() }
                            )
                        }

                        composable("edit_campus_department/{id}") { backStackEntry ->
                            // Grab the database ID passed from the Manage screen
                            val deptId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0

                            EditCampusDepartmentScreen(
                                viewModel = campusViewModel, // Pass the view model
                                departmentId = deptId,       // Pass the ID
                                onBackClick = { navController.popBackStack() },
                                onUpdate    = { navController.popBackStack() }
                            )
                        }

                        // --- STUDENT BOTTOM NAV FEATURES ---

                        composable("schedule/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            ScheduleAndTaskScreen(
                                taskViewModel    = taskViewModel,
                                onDashboardClick = {
                                    navController.navigate("dashboard/${role}//") {
                                        popUpTo("dashboard/${role}//") { inclusive = true }
                                    }
                                },
                                onCampusClick   = { navController.navigate("campus/${role}") },
                                onSettingsClick = { navController.navigate("settings/${role}") }
                            )
                        }

                        composable("campus/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            CampusInformationScreen(
                                viewModel = campusViewModel,
                                onDashboardClick = {
                                    navController.navigate("dashboard/${role}//") {
                                        popUpTo("dashboard/${role}//") { inclusive = true }
                                    }
                                },
                                onScheduleClick = { navController.navigate("schedule/${role}") },
                                onSettingsClick = { navController.navigate("settings/${role}") }
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
                                onScheduleClick = { navController.navigate("schedule/${role}") },
                                onCampusClick   = { navController.navigate("campus/${role}") },
                                onLogoutClick   = {
                                    navController.navigate("get_started") { popUpTo(0) { inclusive = true } }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}