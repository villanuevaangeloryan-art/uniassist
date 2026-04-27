package com.example.uniassist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

private val GreenDark       = Color(0xFF1B5E20)
private val GreenMedium     = Color(0xFF56A566)
private val BackgroundCream = Color(0xFFFDFDF8)

// ── SpanStyle presets ─────────────────────────────────────────────────────────
private val styleBold   = SpanStyle(fontWeight = FontWeight.Bold)
private val styleItalic = SpanStyle(fontStyle  = FontStyle.Italic)
private val styleH1     = SpanStyle(fontWeight = FontWeight.Black,     fontSize = 24.sp)
private val styleH2     = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
private val styleH3     = SpanStyle(fontWeight = FontWeight.Bold,      fontSize = 17.sp)
private val styleH4     = SpanStyle(fontWeight = FontWeight.SemiBold,  fontSize = 15.sp)

// ── Data class ────────────────────────────────────────────────────────────────
data class NewAnnouncement(
    val title      : String,
    val category   : String,
    val isPriority : Boolean,
    val message    : String,
    val postDate   : String,
    val endDate    : String
)

// ── Main Screen ───────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnnouncementScreen(
    onBackClick : () -> Unit = {},
    onSaveDraft : (NewAnnouncement) -> Unit = {},
    onPublish   : (NewAnnouncement) -> Unit = {}
) {
    var title      by remember { mutableStateOf("") }
    var category   by remember { mutableStateOf("Emergency") }
    var isPriority by remember { mutableStateOf(true) }

    // ── FIX: use val + mutableStateOf (not var + by) to preserve AnnotatedString spans
    val richText = remember { mutableStateOf(TextFieldValue("")) }

    var postDate by remember { mutableStateOf(Date()) }
    var endDate  by remember { mutableStateOf(Date()) }

    var showPostDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker  by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) }

    var profileMenuExpanded by remember { mutableStateOf(false) }

    val isPublishEnabled = title.isNotBlank() && richText.value.text.isNotBlank()

    Scaffold(containerColor = BackgroundCream) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF7CA786), Color(0xFFE2F1C6)))
                    )
            ) {
                Row(
                    modifier              = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, "Back",
                            tint = GreenDark, modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        "Create Announcement",
                        fontFamily = Poppins, fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = GreenDark
                    )
                    Box {
                        IconButton(onClick = { profileMenuExpanded = true }) {
                            Box(
                                modifier = Modifier.size(34.dp).border(2.dp, GreenDark, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountCircle, "Profile",
                                    tint = GreenDark, modifier = Modifier.size(30.dp))
                            }
                        }
                        DropdownMenu(
                            expanded = profileMenuExpanded,
                            onDismissRequest = { profileMenuExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Drafts", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Description, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Archive", fontFamily = Poppins, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Outlined.Archive, null, modifier = Modifier.size(16.dp)) },
                                onClick = { profileMenuExpanded = false }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                // ── Title ──────────────────────────────────────────────────────
                InputLabel("Title")
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Enter announcement title", color = Color.LightGray, fontSize = 14.sp) },
                    textStyle = androidx.compose.ui.text.TextStyle(fontFamily = Poppins, fontSize = 14.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor      = GreenMedium,
                        unfocusedBorderColor    = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor   = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Category ───────────────────────────────────────────────────
                InputLabel("Category")
                CategoryDropdownMenu(
                    selectedCategory   = category,
                    onCategorySelected = { category = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Priority ───────────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputLabel("Priority", modifier = Modifier.padding(0.dp))
                    Switch(
                        checked = isPriority, onCheckedChange = { isPriority = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor   = Color.White,
                            checkedTrackColor   = GreenMedium,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Rich Text Message ──────────────────────────────────────────
                InputLabel("Message")
                RichTextEditor(
                    value         = richText.value,
                    onValueChange = { richText.value = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Post Date ──────────────────────────────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InputLabel("Post Date", modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = dateFormatter.format(postDate), onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.weight(1.5f).height(50.dp).clickable { showPostDatePicker = true },
                        shape = RoundedCornerShape(8.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = Poppins, fontSize = 14.sp),
                        trailingIcon = {
                            IconButton(onClick = { showPostDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(18.dp), tint = GreenDark)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor   = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── End Date ───────────────────────────────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InputLabel("End Date", modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = dateFormatter.format(endDate), onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.weight(1.5f).height(50.dp).clickable { showEndDatePicker = true },
                        shape = RoundedCornerShape(8.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = Poppins, fontSize = 14.sp),
                        trailingIcon = {
                            IconButton(onClick = { showEndDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(18.dp), tint = GreenDark)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor   = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ── Action Buttons ─────────────────────────────────────────────
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = {
                            onSaveDraft(NewAnnouncement(
                                title, category, isPriority, richText.value.text,
                                dateFormatter.format(postDate), dateFormatter.format(endDate)
                            ))
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape    = RoundedCornerShape(8.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5E3E)),
                        enabled  = title.isNotBlank()
                    ) {
                        Text("SAVE DRAFT", fontFamily = Poppins, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            onPublish(NewAnnouncement(
                                title, category, isPriority, richText.value.text,
                                dateFormatter.format(postDate), dateFormatter.format(endDate)
                            ))
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape    = RoundedCornerShape(8.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                        enabled  = isPublishEnabled
                    ) {
                        Text("PUBLISH", fontFamily = Poppins, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // ── Date Pickers ──────────────────────────────────────────────────────────
    if (showPostDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = postDate.time)
        DatePickerDialog(
            onDismissRequest = { showPostDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { postDate = Date(it) }
                    showPostDatePicker = false
                }) { Text("OK", color = Color(0xFF245B3D)) }
            },
            dismissButton = {
                TextButton(onClick = { showPostDatePicker = false }) { Text("Cancel", color = Color(0xFF666666)) }
            }
        ) {
            DatePicker(state = state, colors = DatePickerDefaults.colors(
                selectedDayContainerColor = Color(0xFF245B3D),
                todayContentColor         = Color(0xFF245B3D),
                todayDateBorderColor      = Color(0xFF245B3D)
            ))
        }
    }

    if (showEndDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = endDate.time)
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { endDate = Date(it) }
                    showEndDatePicker = false
                }) { Text("OK", color = Color(0xFF245B3D)) }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel", color = Color(0xFF666666)) }
            }
        ) {
            DatePicker(state = state, colors = DatePickerDefaults.colors(
                selectedDayContainerColor = Color(0xFF245B3D),
                todayContentColor         = Color(0xFF245B3D),
                todayDateBorderColor      = Color(0xFF245B3D)
            ))
        }
    }
}

// ── Rich Text Editor ──────────────────────────────────────────────────────────
@Composable
fun RichTextEditor(
    value         : TextFieldValue,
    onValueChange : (TextFieldValue) -> Unit,
    modifier      : Modifier = Modifier
) {
    val activeStyles = remember(value) { getActiveStyles(value) }

    Column(modifier = modifier) {
        // ── Toolbar ────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5FBF5), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .border(
                    1.dp, Color.Gray.copy(alpha = 0.4f),
                    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            FormatButton(label = "B", isActive = activeStyles.contains(RichFormatStyle.BOLD), weight = FontWeight.Bold) {
                val toggled = toggleSpanStyle(value, styleBold)
                onValueChange(toggled.copy(selection = value.selection))
            }
            FormatButton(label = "I", isActive = activeStyles.contains(RichFormatStyle.ITALIC), fontStyle = FontStyle.Italic) {
                val toggled = toggleSpanStyle(value, styleItalic)
                onValueChange(toggled.copy(selection = value.selection))
            }
            Box(modifier = Modifier.height(22.dp).width(1.dp).background(Color.LightGray))
            FormatButton(label = "H1", isActive = activeStyles.contains(RichFormatStyle.H1)) {
                val toggled = toggleSpanStyle(value, styleH1)
                onValueChange(toggled.copy(selection = value.selection))
            }
            FormatButton(label = "H2", isActive = activeStyles.contains(RichFormatStyle.H2)) {
                val toggled = toggleSpanStyle(value, styleH2)
                onValueChange(toggled.copy(selection = value.selection))
            }
            FormatButton(label = "H3", isActive = activeStyles.contains(RichFormatStyle.H3)) {
                val toggled = toggleSpanStyle(value, styleH3)
                onValueChange(toggled.copy(selection = value.selection))
            }
            FormatButton(label = "H4", isActive = activeStyles.contains(RichFormatStyle.H4)) {
                val toggled = toggleSpanStyle(value, styleH4)
                onValueChange(toggled.copy(selection = value.selection))
            }
        }

        // ── Text Area ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp, max = 280.dp)
                .background(Color.White)
                .border(
                    1.dp, Color.Gray.copy(alpha = 0.4f),
                    RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                )
                .padding(12.dp)
        ) {
            BasicTextField(
                value         = value,
                onValueChange = onValueChange,
                modifier      = Modifier.fillMaxWidth(),
                cursorBrush   = SolidColor(GreenDark),
                textStyle     = androidx.compose.ui.text.TextStyle(
                    fontFamily = Poppins,
                    fontSize   = 14.sp,
                    color      = Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(
                            "Enter the announcement details here . . .",
                            fontFamily = Poppins,
                            color      = Color.LightGray,
                            fontSize   = 14.sp
                        )
                    }
                    innerTextField()
                }
            )
        }

        Text(
            text       = "Select text first, then tap a format button to apply styling",
            fontFamily = Poppins,
            fontSize   = 10.sp,
            color      = Color(0xFF9E9E9E),
            modifier   = Modifier.padding(top = 4.dp, start = 2.dp)
        )
    }
}

// ── Format Button ─────────────────────────────────────────────────────────────
@Composable
private fun FormatButton(
    label     : String,
    isActive  : Boolean,
    weight    : FontWeight = FontWeight.SemiBold,
    fontStyle : FontStyle  = FontStyle.Normal,
    onClick   : () -> Unit
) {
    val bgColor   = if (isActive) GreenMedium.copy(alpha = 0.18f) else Color.Transparent
    val textColor = if (isActive) GreenDark else Color.DarkGray
    val borderClr = if (isActive) GreenMedium else Color.Transparent

    Box(
        modifier = Modifier
            .size(width = 34.dp, height = 30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderClr, RoundedCornerShape(6.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            fontFamily = Poppins,
            fontWeight = weight,
            fontStyle  = fontStyle,
            fontSize   = 12.sp,
            color      = textColor
        )
    }
}

// ── Span helpers ──────────────────────────────────────────────────────────────
private enum class RichFormatStyle { BOLD, ITALIC, H1, H2, H3, H4 }

private fun getActiveStyles(tfv: TextFieldValue): Set<RichFormatStyle> {
    val result = mutableSetOf<RichFormatStyle>()
    val spans  = tfv.annotatedString.spanStyles
    val mid    = if (tfv.selection.collapsed) tfv.selection.start
    else (tfv.selection.min + tfv.selection.max) / 2
    for (range in spans) {
        if (mid < range.start || mid > range.end) continue
        val s = range.item
        when {
            s.fontWeight == FontWeight.Black                            -> result += RichFormatStyle.H1
            s.fontWeight == FontWeight.ExtraBold                        -> result += RichFormatStyle.H2
            s.fontWeight == FontWeight.Bold && s.fontSize == 17.sp      -> result += RichFormatStyle.H3
            s.fontWeight == FontWeight.SemiBold && s.fontSize == 15.sp  -> result += RichFormatStyle.H4
            s.fontWeight == FontWeight.Bold                             -> result += RichFormatStyle.BOLD
            s.fontStyle  == FontStyle.Italic                            -> result += RichFormatStyle.ITALIC
        }
    }
    return result
}

private fun toggleSpanStyle(tfv: TextFieldValue, style: SpanStyle): TextFieldValue {
    val start = tfv.selection.min
    val end   = tfv.selection.max
    if (start == end) return tfv

    val existing = tfv.annotatedString
    val alreadyCovered = existing.spanStyles.any { r ->
        r.item == style && r.start <= start && r.end >= end
    }

    val newAnnotated = buildAnnotatedString {
        append(existing.text)
        for (r in existing.spanStyles) {
            if (alreadyCovered && r.item == style && r.start <= start && r.end >= end) continue
            addStyle(r.item, r.start, r.end)
        }
        if (!alreadyCovered) addStyle(style, start, end)
    }

    return tfv.copy(annotatedString = newAnnotated, selection = tfv.selection)
}

// ── Shared sub-composables ────────────────────────────────────────────────────
@Composable
private fun InputLabel(text: String, modifier: Modifier = Modifier.padding(bottom = 8.dp)) {
    Text(
        text       = text,
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize   = 16.sp,
        color      = GreenDark,
        modifier   = modifier
    )
}

@Composable
private fun CategoryDropdownMenu(
    selectedCategory   : String,
    onCategorySelected : (String) -> Unit
) {
    var expanded      by remember { mutableStateOf(false) }
    var activeSubMenu by remember { mutableStateOf<String?>(null) }

    val categories = mapOf(
        "Academic"  to listOf("Exams", "Enrollment", "Deadlines", "Scholarship updates"),
        "Event"     to listOf("Campus festival", "Org activities", "Sports fest"),
        "General"   to listOf("Office hours", "Policy reminders", "General updates"),
        "Emergency" to listOf("Class suspension", "Typhoon alerts", "Safety notices")
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCategory, onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = Poppins, fontSize = 14.sp),
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, tint = GreenMedium) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = GreenMedium,
                unfocusedBorderColor    = Color.Gray.copy(alpha = 0.5f),
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        Box(modifier = Modifier.matchParentSize().clickable { expanded = true })
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false; activeSubMenu = null },
            modifier = Modifier.background(Color.White)
        ) {
            if (activeSubMenu == null) {
                categories.keys.forEach { mainCat ->
                    DropdownMenuItem(
                        text         = { Text(mainCat, fontFamily = Poppins, color = Color.DarkGray) },
                        trailingIcon = { Icon(Icons.Default.ChevronRight, null, tint = GreenMedium) },
                        onClick      = { activeSubMenu = mainCat }
                    )
                }
            } else {
                DropdownMenuItem(
                    text    = { Text("← Back to Categories", fontFamily = Poppins, color = Color.Gray, fontSize = 12.sp) },
                    onClick = { activeSubMenu = null }
                )
                HorizontalDivider()
                categories[activeSubMenu]?.forEach { subCat ->
                    DropdownMenuItem(
                        text    = { Text(subCat, fontFamily = Poppins, color = Color.DarkGray) },
                        onClick = {
                            onCategorySelected(subCat)
                            expanded      = false
                            activeSubMenu = null
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateAnnouncementPreview() {
    MaterialTheme { CreateAnnouncementScreen() }
}