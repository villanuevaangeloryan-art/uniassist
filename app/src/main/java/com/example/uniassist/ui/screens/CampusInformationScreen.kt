package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R
import com.example.uniassist.ui.theme.UniassistTheme

data class Department(
    val name: String,
    val representative: String,
    val email: String,
    val phone: String,
    val logoRes: Int
)

@Composable
fun CampusInformationScreen(
    onDashboardClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            CampusHeaderSection()   // RENAMED
        },
        bottomBar = {
            CampusBottomNavigationBar(
                onDashboardClick = onDashboardClick,
                onScheduleClick = onScheduleClick,
                onSettingsClick = onSettingsClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                TitleSection()
            }
            item {
                Text(
                    text = "College Departments",
                    color = Color(0xFF1B5E20),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.5.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(departments) { department ->
                DepartmentCard(department)
            }
        }
    }
}

// RENAMED from HeaderSection to CampusHeaderSection
@Composable
fun CampusHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF7CA786),
                        Color(0xFFE2F1C6)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun CampusBottomNavigationBar(
    onDashboardClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .height(58.dp),
        shape = RoundedCornerShape(35.dp),
        color = Color(0xFF245B3D),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Dashboard",
                isSelected = false,
                onClick = onDashboardClick
            )
            BottomNavItem(
                icon = Icons.Default.DateRange,
                label = "Schedules",
                isSelected = false,
                onClick = onScheduleClick
            )
            BottomNavItem(
                icon = Icons.Default.Info,
                label = "Campus",
                isSelected = true,
                onClick = {}
            )
            BottomNavItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                isSelected = false,
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
fun TitleSection() {
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = Color(0xFFD32F2F),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.campusinfologo),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Campus Information",
                color = Color(0xFF1B5E20),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                textAlign = TextAlign.Left
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "To assist with your inquiries and help you navigate our academic offerings, we have provided an overview of our university's structure below...",
            textAlign = TextAlign.Justify,
            color = Color(0xFF2E7D32),
            fontSize = 14.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 2.dp)
        )
    }
}

@Composable
fun DepartmentCard(dept: Department) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, Color(0xFF66BB6A), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = dept.logoRes),
                contentDescription = "Dept Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = dept.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Department representative: ${dept.representative}",
                    fontSize = 12.sp,
                    color = Color.Black
                )
                Text(
                    text = "Email: ${dept.email}",
                    fontSize = 12.sp,
                    color = Color.Black
                )
                Text(
                    text = "Phone number: ${dept.phone}",
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampusScreenPreview() {
    UniassistTheme {
        CampusInformationScreen()
    }
}