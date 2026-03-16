package com.example.uniassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.uniassist.ui.screens.ManageScreenAnnouncement
import com.example.uniassist.ui.screens.CreateAnnouncementScreen
import com.example.uniassist.ui.screens.ManageCampusDepartmentScreen
import com.example.uniassist.ui.screens.AddCampusDepartmentScreen
import com.example.uniassist.ui.screens.EditCampusDepartmentScreen
import com.example.uniassist.ui.screens.Department
import com.example.uniassist.ui.screens.toManageCard

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

                    // ── Shared list of published announcements ──────────────────
                    // Declared here (above NavHost) so it survives navigation.
                    // ManageAnnouncementsScreen reads it; CreateAnnouncementScreen
                    // writes to it via the onPublish callback below.
                    val publishedAnnouncements = remember { mutableStateListOf<ManageScreenAnnouncement>() }

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

                        // ── Manage Announcements ────────────────────────────────
                        // FIX: Now receives publishedAnnouncements so newly
                        // published items appear in the list automatically.
                        composable("manage_announcements") {
                            ManageAnnouncementsScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onCreateClick = {
                                    navController.navigate("create_announcement")
                                },
                                publishedAnnouncements = publishedAnnouncements
                            )
                        }

                        composable("manage_campus_department") {
                            ManageCampusDepartmentScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onAddDepartmentClick = {
                                    navController.navigate("add_campus_department")
                                },
                                onEditDepartmentClick = { index ->
                                    navController.navigate("edit_campus_department/$index")
                                }
                            )
                        }

                        composable("add_campus_department") {
                            AddCampusDepartmentScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onSaveDraft = { department ->
                                    navController.popBackStack()
                                },
                                onPublish = { department ->
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("edit_campus_department/{index}") { backStackEntry ->
                            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                            val departments = listOf(
                                Department(
                                    name = "College of Computer Studies / Information Technology",
                                    representative = "Mr. Alvin M. Dimaunahan",
                                    email = "alvinccsdimauanahan@gmail.com",
                                    phone = "+639829901987",
                                    logoRes = R.drawable.ccsdeptlogo
                                ),
                                Department(
                                    name = "College of Engineering",
                                    representative = "Ms. Jennelyn V. Tan",
                                    email = "tanjennelyncoe@gmail.com",
                                    phone = "+639549932855",
                                    logoRes = R.drawable.coedeptlogo
                                ),
                                Department(
                                    name = "College of Business Administration / Accountancy",
                                    representative = "Ms. Vilma R. Cacho",
                                    email = "rcachocbaa@gmail.com",
                                    phone = "+639549932855",
                                    logoRes = R.drawable.cbaadeptlogo
                                ),
                                Department(
                                    name = "College of Education",
                                    representative = "Ms. George C. Bacsa",
                                    email = "geocalmacoedue@gmail.com",
                                    phone = "+639877718241",
                                    logoRes = R.drawable.coedudeptlogo
                                )
                            )
                            val department = departments.getOrNull(index) ?: departments[0]

                            EditCampusDepartmentScreen(
                                department = department,
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onUpdate = { updatedDept ->
                                    navController.popBackStack()
                                }
                            )
                        }

                        // ── Create Announcement ─────────────────────────────────
                        // FIX: Added onPublish lambda. It converts the form data
                        // into a ManageScreenAnnouncement, prepends it to the
                        // shared list, then navigates back — the list is already
                        // updated when ManageAnnouncementsScreen recomposes.
                        composable("create_announcement") {
                            CreateAnnouncementScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onSaveDraft = {
                                    navController.popBackStack()
                                },
                                onPublish = { newAnnouncement ->
                                    publishedAnnouncements.add(
                                        0, // insert at top of the list
                                        newAnnouncement.toManageCard(
                                            id = publishedAnnouncements.size + 100
                                        )
                                    )
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