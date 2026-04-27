package com.example.uniassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.uniassist.ui.theme.DarkGreen

// ─────────────────────────────────────────────────────────────────────────────
// EDIT DRAFT ANNOUNCEMENT SCREEN
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Screen for editing an existing draft announcement.
 *
 * @param draft             The draft [Announcement] to be edited. Pass a blank
 *                          [Announcement] (id = 0) to create a new draft.
 * @param onBackClick       Navigate back without saving.
 * @param onSaveDraft       Save updated content as a draft (keep isDraft = true).
 * @param onPublish         Publish the draft immediately (clears isDraft flag).
 */
@Composable
fun EditAnnouncementScreen(
    draft: Announcement = Announcement(
        id = 0, title = "", date = "", content = "",
        bullets = emptyList(), footer = "", category = "",
        isArchived = false, isDraft = true
    ),
    onBackClick: () -> Unit = {},
    onSaveDraft: (Announcement, String, Boolean) -> Unit = { _, _, _ -> },
    onPublish: (Announcement, String, Boolean) -> Unit = { _, _, _ -> }
) {
    // ── Form state ──────────────────────────────────────────────────────────
    var title by remember { mutableStateOf(draft.title) }
    var date by remember { mutableStateOf(draft.date) }
    var content by remember { mutableStateOf(draft.content) }
    var footer by remember { mutableStateOf(draft.footer) }
    var category by remember { mutableStateOf(draft.category) }
    var isPriority by remember { mutableStateOf(false) }

    var bullets by remember {
        mutableStateOf<List<Pair<String, String>>>(
            if (draft.bullets.isEmpty()) emptyList()
            else draft.bullets.toList()
        )
    }

    var showPublishDialog by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    // Helper to build the current Announcement from form state
    fun buildAnnouncement() = draft.copy(
        title   = title.trim(),
        date    = date.trim(),
        content = content.trim(),
        bullets = bullets.toList(),
        footer  = footer.trim(),
        category = category.trim(),
        isDraft  = true
    )

    Scaffold(
        topBar = {
            EditTopBar(
                isNewDraft = draft.id == 0,
                onBackClick = { showDiscardDialog = true }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFDFB))
        ) {
            // Background texture
            Image(
                painter = painterResource(id = R.drawable.background_rolepick),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.12f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ── Section header ─────────────────────────────────────────
                SectionLabel(text = "Announcement Details")

                // ── Title ──────────────────────────────────────────────────
                EditField(
                    label = "Title",
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Enter announcement title"
                )

                // ── Date ───────────────────────────────────────────────────
                EditField(
                    label = "Date",
                    value = date,
                    onValueChange = { date = it },
                    placeholder = "e.g. June 15, 2025"
                )

                // ── Category + Priority row ────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EditField(
                        label = "Category",
                        value = category,
                        onValueChange = { category = it },
                        placeholder = "e.g. Academic, Events",
                        modifier = Modifier.weight(1f)
                    )

                    // Priority toggle
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Priority",
                            fontFamily = Poppins,
                            fontSize = 11.sp,
                            color = Color(0xFF5A7A65)
                        )
                        Switch(
                            checked = isPriority,
                            onCheckedChange = { isPriority = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF2D6A4F),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFBDBDBD)
                            )
                        )
                    }
                }

                // ── Content ────────────────────────────────────────────────
                SectionLabel(text = "Content")

                EditField(
                    label = "Message / Body",
                    value = content,
                    onValueChange = { content = it },
                    placeholder = "Write the announcement content here...",
                    singleLine = false,
                    minLines = 4,
                    maxLines = 8
                )

                // ── Bullet Points ──────────────────────────────────────────
                SectionLabel(text = "Bullet Points (Optional)")

                bullets.forEachIndexed { index, bullet ->
                    BulletRow(
                        label = bullet.first,
                        value = bullet.second,
                        onLabelChange = { newLabel ->
                            bullets = bullets.toMutableList().also { it[index] = newLabel to bullet.second }
                        },
                        onValueChange = { newVal ->
                            bullets = bullets.toMutableList().also { it[index] = bullet.first to newVal }
                        },
                        onRemove = {
                            bullets = bullets.toMutableList().also { it.removeAt(index) }
                        }
                    )
                }

                // Add bullet button
                OutlinedButton(
                    onClick = {
                        bullets = bullets.toMutableList().also { it.add("" to "") }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(DarkGreen, DarkGreen))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add Bullet Point", fontFamily = Poppins, fontSize = 13.sp)
                }

                // ── Footer ─────────────────────────────────────────────────
                SectionLabel(text = "Footer (Optional)")

                EditField(
                    label = "Footer",
                    value = footer,
                    onValueChange = { footer = it },
                    placeholder = "e.g. For inquiries, contact the registrar."
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ── Action buttons ─────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Save as Draft
                    OutlinedButton(
                        onClick = { onSaveDraft(buildAnnouncement(), category.trim(), isPriority) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(DarkGreen, DarkGreen))
                        ),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Save Draft", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }

                    // Publish
                    Button(
                        onClick = { showPublishDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Text("Publish", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ── Publish confirmation ───────────────────────────────────────────────
    if (showPublishDialog) {
        AlertDialog(
            onDismissRequest = { showPublishDialog = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFFF5FBF5),
            title = {
                Text(
                    "Publish Announcement",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkGreen
                )
            },
            text = {
                Text(
                    "This will publish the announcement and notify students. Continue?",
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = Color(0xFF3D5A45)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPublishDialog = false
                        onPublish(buildAnnouncement(), category.trim(), isPriority)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F))
                ) {
                    Text("Publish", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPublishDialog = false }) {
                    Text("Cancel", fontFamily = Poppins, color = Color(0xFF5A7A65))
                }
            }
        )
    }

    // ── Discard / back confirmation ────────────────────────────────────────
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFFF5FBF5),
            title = {
                Text(
                    "Discard Changes?",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkGreen
                )
            },
            text = {
                Text(
                    "You have unsaved changes. Do you want to save as draft before leaving?",
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = Color(0xFF3D5A45)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDiscardDialog = false
                        onSaveDraft(buildAnnouncement(), category.trim(), isPriority)
                        onBackClick()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F))
                ) {
                    Text("Save & Exit", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { showDiscardDialog = false }) {
                        Text("Keep Editing", fontFamily = Poppins, color = Color(0xFF5A7A65))
                    }
                    TextButton(onClick = {
                        showDiscardDialog = false
                        onBackClick()
                    }) {
                        Text("Discard", fontFamily = Poppins, color = Color(0xFFD32F2F))
                    }
                }
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// INTERNAL COMPOSABLES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EditTopBar(isNewDraft: Boolean, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF7CA786), Color(0xFFE2F1C6))
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
                text = if (isNewDraft) "New Draft" else "Edit Draft",
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
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = Poppins,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF5A7A65)
    )
}

@Composable
private fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 11.sp,
            color = Color(0xFF5A7A65),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = Color(0xFFAECFB4)
                )
            },
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D6A4F),
                unfocusedBorderColor = Color(0xFFB0CCBA),
                focusedContainerColor = Color(0xFFF0FAF2),
                unfocusedContainerColor = Color(0xFFF9FDF9),
                cursorColor = Color(0xFF2D6A4F),
                focusedTextColor = DarkGreen,
                unfocusedTextColor = DarkGreen
            ),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = Poppins,
                fontSize = 13.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BulletRow(
    label: String,
    value: String,
    onLabelChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEAF4EB), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFB0CCBA), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = label,
            onValueChange = onLabelChange,
            placeholder = {
                Text("Label", fontFamily = Poppins, fontSize = 12.sp, color = Color(0xFFAECFB4))
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D6A4F),
                unfocusedBorderColor = Color(0xFFB0CCBA),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFF2D6A4F),
                focusedTextColor = DarkGreen,
                unfocusedTextColor = DarkGreen
            ),
            textStyle = LocalTextStyle.current.copy(fontFamily = Poppins, fontSize = 12.sp),
            modifier = Modifier.weight(0.4f)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text("Value", fontFamily = Poppins, fontSize = 12.sp, color = Color(0xFFAECFB4))
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D6A4F),
                unfocusedBorderColor = Color(0xFFB0CCBA),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFF2D6A4F),
                focusedTextColor = DarkGreen,
                unfocusedTextColor = DarkGreen
            ),
            textStyle = LocalTextStyle.current.copy(fontFamily = Poppins, fontSize = 12.sp),
            modifier = Modifier.weight(0.6f)
        )

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun PreviewEditAnnouncementScreen() {
    EditAnnouncementScreen(
        draft = Announcement(
            id = 1,
            title = "Enrollment Reminder",
            date = "June 15, 2025",
            content = "Please complete your enrollment before the deadline.",
            bullets = listOf("Deadline" to "June 20, 2025", "Venue" to "Registrar's Office"),
            footer = "For inquiries, contact the registrar.",
            category = "Academic",
            isArchived = false,
            isDraft = true
        )
    )
}