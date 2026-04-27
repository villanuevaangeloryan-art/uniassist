package com.example.uniassist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
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

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* permission result */ }

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

        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            UniassistTheme(darkTheme = isDarkMode) {
                Surface(modifier = Modifier.fillMaxSize(), color = if (isDarkMode) Color(0xFF121212) else Color.White) {
                    val navController = rememberNavController()

                    val allAnnouncements      by announcementViewModel.announcements.collectAsState()
                    val draftAnnouncements    by announcementViewModel.draftAnnouncements.collectAsState()
                    val archivedAnnouncements by announcementViewModel.archivedAnnouncements.collectAsState()

                    val dbManageList = allAnnouncements
                        .filter { !it.isDraft }
                        .map { it.toManageCard() }

                    NavHost(navController = navController, startDestination = "get_started") {

                        // ── AUTHENTICATION & ONBOARDING ───────────────────────

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
                            LoginOrRegisterScreen(navController = navController, role = role)
                        }

                        composable("complete_profile/{email}/{role}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            val role  = backStackEntry.arguments?.getString("role")  ?: "Student"
                            CompleteProfileScreen(navController, email, role)
                        }

                        // ── DASHBOARDS ────────────────────────────────────────

                        composable("dashboard/{role}/{firstName}/{lastName}") { backStackEntry ->
                            val role      = backStackEntry.arguments?.getString("role")      ?: "Student"
                            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
                            val lastName  = backStackEntry.arguments?.getString("lastName")  ?: ""
                            val fullName  = if (firstName.isNotBlank() && lastName.isNotBlank()) {
                                "$firstName $lastName"
                            } else {
                                if (role == "Teacher") "Prof. Angelo Villanueva" else "Angelo Villanueva"
                            }

                            if (role == "Teacher") {
                                TeacherDashboardScreen(
                                    userName = fullName,
                                    totalStudents = 128,
                                    totalAnnouncements = 23,
                                    totalUnread = 5,
                                    currentRoute = "dashboard",
                                    announcementViewModel = announcementViewModel,
                                    onDashboardClick = { },
                                    onDraftsClick = { navController.navigate("drafts_menu") },
                                    onArchiveClick = { navController.navigate("archive_menu") },
                                    onSettingsClick = { navController.navigate("settings/${role}") },
                                    onManageAnnouncements = { navController.navigate("manage_announcements") },
                                    onManageCampusInfo = { navController.navigate("manage_campus_department") },
                                    onViewStudents = { navController.navigate("view_students") },
                                    onLogout = {
                                        navController.navigate("get_started") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            } else {
                                DashboardScreen(
                                    role                  = role,
                                    firstName             = firstName,
                                    lastName              = lastName,
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

                        // ── TEACHER FEATURES ──────────────────────────────────

                        composable("view_students") {
                            StudentListScreen(
                                viewModel   = studentViewModel,
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                        composable("manage_announcements") {
                            ManageAnnouncementsScreen(
                                onBackClick     = { navController.popBackStack() },
                                onCreateClick   = { navController.navigate("create_announcement") },
                                dbAnnouncements = dbManageList,
                                onArchive       = { id -> announcementViewModel.archive(id) },
                                onUnarchive     = { id -> announcementViewModel.unarchive(id) },
                                onDelete        = { card ->
                                    val target = allAnnouncements.find { it.id == card.id }
                                    if (target != null) announcementViewModel.deleteAnnouncement(target)
                                },
                                onEditNavigate  = { id -> navController.navigate("edit_announcement/$id") }
                            )
                        }

                        composable("edit_announcement/{id}") { backStackEntry ->
                            val id           = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                            val announcement = allAnnouncements.find { it.id == id }
                            if (announcement != null) {
                                EditAnnouncementScreen(
                                    draft       = announcement,
                                    onBackClick = { navController.popBackStack() },
                                    onSaveDraft = { ann, cat, priority ->
                                        announcementViewModel.updateDraft(ann, cat, priority)
                                        navController.popBackStack()
                                    },
                                    onPublish = { ann, cat, priority ->
                                        announcementViewModel.publish(ann, cat, priority)
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        composable("create_announcement") {
                            CreateAnnouncementScreen(
                                onBackClick = { navController.navigateUp() },
                                onSaveDraft = { newAnnouncement ->
                                    announcementViewModel.saveDraft(
                                        announcement = Announcement(
                                            title    = newAnnouncement.title,
                                            date     = newAnnouncement.postDate,
                                            content  = newAnnouncement.message,
                                            bullets  = emptyList(),
                                            footer   = if (newAnnouncement.isPriority) "⚠ Priority Announcement" else "",
                                            category = newAnnouncement.category,
                                            isDraft  = true
                                        ),
                                        category   = newAnnouncement.category,
                                        isPriority = newAnnouncement.isPriority
                                    )
                                    navController.navigateUp()
                                },
                                onPublish = { newAnnouncement ->
                                    announcementViewModel.publish(
                                        announcement = Announcement(
                                            title    = newAnnouncement.title,
                                            date     = newAnnouncement.postDate,
                                            content  = newAnnouncement.message,
                                            bullets  = emptyList(),
                                            footer   = if (newAnnouncement.isPriority) "⚠ Priority Announcement" else "",
                                            category = newAnnouncement.category
                                        ),
                                        category   = newAnnouncement.category,
                                        isPriority = newAnnouncement.isPriority
                                    )
                                    navController.navigateUp()
                                }
                            )
                        }

                        // ── DRAFTS MENU ───────────────────────────────────────

                        composable("drafts_menu") {
                            DraftsMenuScreen(
                                onDashboardClick = {
                                    navController.navigate("dashboard/Teacher//") {
                                        popUpTo("dashboard/Teacher//") { inclusive = true }
                                    }
                                },
                                onSettingsClick = { navController.navigate("settings/Teacher") },
                                onNavigate = { item ->
                                    when (item) {
                                        "Announcements Draft" -> navController.navigate("announcements_draft")
                                        "Archive Menu" -> navController.navigate("archive_menu")
                                    }
                                }
                            )
                        }

                        // ── ANNOUNCEMENTS DRAFT LIST ──────────────────────────

                        composable("announcements_draft") {
                            AnnouncementsDraftScreen(
                                drafts = draftAnnouncements,
                                onDashboardClick = {
                                    navController.navigate("dashboard/Teacher//") {
                                        popUpTo("dashboard/Teacher//") { inclusive = true }
                                    }
                                },
                                onArchiveClick = { navController.navigate("archive_menu") },
                                onSettingsClick = { navController.navigate("settings/Teacher") },
                                onEditDraft = { draft ->
                                    navController.navigate("edit_draft/${draft.id}")
                                },
                                onPublishDraft = { draft ->
                                    announcementViewModel.publishDraft(draft)
                                },
                                onDeleteDraft = { draft ->
                                    announcementViewModel.deleteAnnouncement(draft)
                                }
                            )
                        }

                        // ── EDIT DRAFT ────────────────────────────────────────

                        composable("edit_draft/{draftId}") { backStackEntry ->
                            val draftId = backStackEntry.arguments?.getString("draftId")?.toIntOrNull() ?: 0
                            val draft = draftAnnouncements.find { it.id == draftId }
                                ?: Announcement(
                                    id       = 0,
                                    title    = "",
                                    date     = "",
                                    content  = "",
                                    bullets  = emptyList(),
                                    footer   = "",
                                    category = "",
                                    isDraft  = true
                                )

                            EditAnnouncementScreen(
                                draft       = draft,
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onSaveDraft = { updated, category, isPriority ->
                                    announcementViewModel.updateDraft(updated, category, isPriority)
                                    navController.popBackStack()
                                },
                                onPublish = { updated, category, isPriority ->
                                    announcementViewModel.updateDraft(updated, category, isPriority)
                                    announcementViewModel.publishDraft(updated)
                                    navController.popBackStack()
                                }
                            )
                        }

                        // ── ARCHIVE MENU ──────────────────────────────────────

                        composable("archive_menu") {
                            ArchiveMenuScreen(
                                onDashboardClick = {
                                    navController.navigate("dashboard/Teacher//") {
                                        popUpTo("dashboard/Teacher//") { inclusive = true }
                                    }
                                },
                                onSettingsClick = { navController.navigate("settings/Teacher") },
                                onNavigate = { item ->
                                    when (item) {
                                        "Announcements Archive" -> navController.navigate("announcements_archive")
                                        "Drafts Menu" -> navController.navigate("drafts_menu")
                                    }
                                }
                            )
                        }

                        // ── ANNOUNCEMENTS ARCHIVE LIST ────────────────────────

                        composable("announcements_archive") {
                            AnnouncementsArchiveScreen(
                                archived = archivedAnnouncements,
                                onDashboardClick = {
                                    navController.navigate("dashboard/Teacher//") {
                                        popUpTo("dashboard/Teacher//") { inclusive = true }
                                    }
                                },
                                onDraftsClick = { navController.navigate("drafts_menu") },
                                onSettingsClick = { navController.navigate("settings/Teacher") },
                                onUnarchive = { announcement ->
                                    announcementViewModel.unarchive(announcement.id)
                                },
                                onDelete = { announcement ->
                                    announcementViewModel.deleteAnnouncement(announcement)
                                }
                            )
                        }

                        // ── CAMPUS MANAGEMENT ─────────────────────────────────

                        composable("manage_campus_department") {
                            ManageCampusDepartmentScreen(
                                viewModel             = campusViewModel,
                                onBackClick           = { navController.navigateUp() },
                                onAddDepartmentClick  = { navController.navigate("add_campus_department") },
                                onEditDepartmentClick = { index ->
                                    navController.navigate("edit_campus_department/$index")
                                }
                            )
                        }

                        composable("add_campus_department") {
                            AddCampusDepartmentScreen(
                                viewModel   = campusViewModel,
                                onBackClick = { navController.navigateUp() },
                                onSaveDraft = { navController.navigateUp() },
                                onPublish   = { navController.navigateUp() }
                            )
                        }

                        composable("edit_campus_department/{id}") { backStackEntry ->
                            val deptId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                            EditCampusDepartmentScreen(
                                viewModel    = campusViewModel,
                                departmentId = deptId,
                                onBackClick  = { navController.navigateUp() },
                                onUpdate     = { navController.navigateUp() }
                            )
                        }

                        // ── STUDENT BOTTOM NAV FEATURES ───────────────────────

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

                        // ── SETTINGS SCREEN ───────────────────────────────────

                        composable("settings/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "Student"
                            SettingsScreen(
                                role = role,
                                isDarkMode = isDarkMode,
                                onDarkModeChange = { isDarkMode = it },
                                onDashboardClick = {
                                    navController.navigate("dashboard/${role}//") {
                                        popUpTo("dashboard/${role}//") { inclusive = true }
                                    }
                                },
                                onScheduleClick = { navController.navigate("schedule/${role}") },
                                onCampusClick   = { navController.navigate("campus/${role}") },
                                onDraftsClick   = { navController.navigate("drafts_menu") },
                                onArchiveClick  = { navController.navigate("archive_menu") },
                                onLogoutClick   = {
                                    com.google.firebase.messaging.FirebaseMessaging.getInstance()
                                        .unsubscribeFromTopic("announcements")
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