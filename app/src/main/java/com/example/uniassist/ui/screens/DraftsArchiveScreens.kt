package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniassist.R

// Using the same DarkGreen from the project theme
private val DarkGreen = Color(0xFF1B4332)

@Composable
fun DraftsMenuScreen(onBackClick: () -> Unit = {}, onNavigate: (String) -> Unit = {}) {
    MenuTemplate(
        title = "Drafts Menu",
        items = listOf("Announcements Draft", "Information Draft"),
        onBackClick = onBackClick,
        onItemClick = onNavigate
    )
}

@Composable
fun ArchiveMenuScreen(onBackClick: () -> Unit = {}, onNavigate: (String) -> Unit = {}) {
    MenuTemplate(
        title = "Archive Menu",
        items = listOf("Announcements Archive", "Information Archive"),
        onBackClick = onBackClick,
        onItemClick = onNavigate
    )
}

@Composable
private fun MenuTemplate(
    title: String,
    items: List<String>,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            MenuTopBar(title = title, onBackClick = onBackClick)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFB))
        ) {
            // Full screen background pattern
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.15f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items.forEach { item ->
                    MenuCard(title = item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
private fun MenuTopBar(title: String, onBackClick: () -> Unit) {
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
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkGreen,
                    modifier = Modifier.size(26.dp)
                )
            }

            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = DarkGreen,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun MenuCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(344.dp)
            .height(86.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD4E7D5).copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Card background pattern filling the card for "selections" feel
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.35f
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDraftsMenu() {
    DraftsMenuScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewArchiveMenu() {
    ArchiveMenuScreen()
}
