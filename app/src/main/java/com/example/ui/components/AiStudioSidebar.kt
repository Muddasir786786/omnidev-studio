package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.OmniDevViewModel

enum class StudioDestination(val title: String, val icon: ImageVector) {
    STUDIO("Prompt Studio", Icons.Default.AutoFixHigh),
    SPLIT_CANVAS("Code & Sandbox", Icons.Default.ViewSidebar),
    PREVIEW("Live Preview", Icons.Default.Visibility),
    SECURITY("Security Audit", Icons.Default.Shield),
    PROJECTS("Project History", Icons.Default.Folder)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiStudioSidebarContent(
    viewModel: OmniDevViewModel,
    currentDestination: StudioDestination,
    onSelectDestination: (StudioDestination) -> Unit,
    onCloseSidebar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedModel by viewModel.selectedModel.collectAsStateWithLifecycle()
    val savedProjects by viewModel.savedProjects.collectAsStateWithLifecycle()
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val customApiKey by viewModel.customApiKey.collectAsStateWithLifecycle()

    var showApiKeyField by remember { mutableStateOf(false) }

    val models = listOf(
        "Gemini 2.5 Flash" to "Ultra-fast code synthesis & live DOM",
        "Gemini 1.5 Pro" to "High-reasoning multi-file architecture",
        "Claude 3.5 Sonnet" to "Fullstack frontend & design system",
        "GPT-4o" to "Autonomous code optimization",
        "Codiffera Sandbox" to "Zero-latency local runtime engine"
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(StudioSurfaceDark)
            .border(
                width = 1.dp,
                color = StudioBorderDark,
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            )
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        // Sidebar Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CodifferaLogoBadge(size = 34.dp, showGlow = true)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Codiff", color = TextPrimaryDark, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                        Text("era", color = StudioCyanPrimary, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Text("AI STUDIO CONSOLE", color = StudioBlueAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                }
            }

            IconButton(
                onClick = onCloseSidebar,
                modifier = Modifier.size(28.dp).testTag("btn_close_sidebar")
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close Menu", tint = TextSecondaryDark, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Navigation Sections
        Text(
            text = "WORKSPACES",
            color = TextMutedDark,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        StudioDestination.values().forEach { dest ->
            val isSelected = currentDestination == dest
            Surface(
                color = if (isSelected) StudioCardHoverDark else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, StudioBlueAccent.copy(alpha = 0.5f)) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .clickable {
                        onSelectDestination(dest)
                        onCloseSidebar()
                    }
                    .testTag("nav_item_${dest.name.lowercase()}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = dest.title,
                        tint = if (isSelected) StudioBlueAccent else TextSecondaryDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = dest.title,
                        color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = StudioBorderDark)
        Spacer(modifier = Modifier.height(16.dp))

        // Models Selection
        Text(
            text = "AI MODEL ENGINE",
            color = TextMutedDark,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        models.forEach { (modelName, desc) ->
            val isSelected = selectedModel == modelName
            Surface(
                color = if (isSelected) StudioCardDark else Color.Transparent,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) StudioBlueAccent else StudioBorderDark.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clickable { viewModel.setSelectedModel(modelName) }
                    .testTag("model_select_${modelName.replace(" ", "_")}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, if (isSelected) StudioBlueAccent else TextMutedDark, CircleShape)
                            .padding(2.dp)
                    ) {
                        if (isSelected) {
                            Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(StudioBlueAccent))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = modelName,
                            color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        Text(
                            text = desc,
                            color = TextMutedDark,
                            fontSize = 10.sp,
                            lineHeight = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = StudioBorderDark)
        Spacer(modifier = Modifier.height(16.dp))

        // Recent Projects History
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SAVED PROJECTS (${savedProjects.size})",
                color = TextMutedDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            IconButton(
                onClick = {
                    onSelectDestination(StudioDestination.PROJECTS)
                    onCloseSidebar()
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.ChevronRight, contentDescription = "View all", tint = TextSecondaryDark, modifier = Modifier.size(16.dp))
            }
        }

        savedProjects.take(4).forEach { proj ->
            val isCurrent = currentProject?.project?.id == proj.id
            Surface(
                color = if (isCurrent) StudioCardDark else Color.Transparent,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .clickable {
                        viewModel.loadProject(proj.id)
                        onSelectDestination(StudioDestination.SPLIT_CANVAS)
                        onCloseSidebar()
                    }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isCurrent) StudioCyanPrimary else StudioBorderDark)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = proj.title,
                            color = if (isCurrent) TextPrimaryDark else TextSecondaryDark,
                            fontSize = 12.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                        Text(
                            text = "${proj.platform} • ${proj.language}",
                            color = TextMutedDark,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = StudioBorderDark)
        Spacer(modifier = Modifier.height(16.dp))

        // API Key Configuration
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Key, contentDescription = null, tint = StudioCyanPrimary, modifier = Modifier.size(16.dp))
                Text("API Credentials", color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            TextButton(
                onClick = { showApiKeyField = !showApiKeyField },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(if (showApiKeyField) "Hide" else "Configure", color = StudioBlueAccent, fontSize = 11.sp)
            }
        }

        if (showApiKeyField) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customApiKey,
                onValueChange = { viewModel.customApiKey.value = it },
                placeholder = { Text("Enter Gemini / AI Studio key", fontSize = 11.sp, color = TextMutedDark) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudioBlueAccent,
                    unfocusedBorderColor = StudioBorderDark,
                    focusedTextColor = TextPrimaryDark,
                    unfocusedTextColor = TextPrimaryDark
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Security Status Footnote
        Surface(
            color = StudioBackgroundDark,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = StudioEmeraldSuccess, modifier = Modifier.size(18.dp))
                Column {
                    Text("Zero-Trust Sandbox", color = TextPrimaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("OWASP Top 10 Hardened & Isolated", color = TextMutedDark, fontSize = 9.sp)
                }
            }
        }
    }
}
