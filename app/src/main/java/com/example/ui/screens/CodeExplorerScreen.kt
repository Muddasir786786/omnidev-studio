package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ArchitectureGraphView
import com.example.ui.components.CodifferaHeroBanner
import com.example.ui.components.CodifferaLogoBadge
import com.example.ui.components.SyntaxHighlightedCodeView
import com.example.ui.preview.LiveSandboxPreviewView
import com.example.ui.theme.*
import com.example.ui.viewmodel.CanvasViewMode
import com.example.ui.viewmodel.OmniDevViewModel

@Composable
fun CodeExplorerScreen(
    viewModel: OmniDevViewModel,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val selectedFileIndex by viewModel.selectedFileIndex.collectAsStateWithLifecycle()
    val canvasViewMode by viewModel.canvasViewMode.collectAsStateWithLifecycle()
    var showArchitecture by remember { mutableStateOf(false) }
    var refinementText by remember { mutableStateOf("") }

    val project = currentProject?.project
    val files = currentProject?.files ?: emptyList()
    val activeFile = files.getOrNull(selectedFileIndex) ?: files.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Prominent Codiffera Header Card with Glowing Startup Splash
        CodifferaHeroBanner(
            subtitle = "DEV TOOL STUDIO • CODE ENGINE & SANDBOX",
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // Split View Code & Architecture Controls Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CodifferaLogoBadge(size = 32.dp, showGlow = true)
                Column {
                    Text("WORKSPACE ENGINE", color = StudioCyanPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(project?.title ?: "Project Workspace", color = TextPrimaryDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Dual View Mode Buttons
            Row(
                modifier = Modifier
                    .background(StudioCardDark, RoundedCornerShape(8.dp))
                    .border(1.dp, StudioBorderDark, RoundedCornerShape(8.dp))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CanvasViewMode.values().forEach { mode ->
                    val isSelected = canvasViewMode == mode
                    Surface(
                        color = if (isSelected) StudioBlueAccent else Color.Transparent,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .clickable { viewModel.setCanvasViewMode(mode) }
                            .testTag("explorer_btn_mode_${mode.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = when (mode) {
                                    CanvasViewMode.CODE -> Icons.Default.Code
                                    CanvasViewMode.LIVE_PREVIEW -> Icons.Default.Visibility
                                    CanvasViewMode.SPLIT -> Icons.Default.ViewSidebar
                                },
                                contentDescription = mode.title,
                                tint = if (isSelected) Color.White else TextSecondaryDark,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = mode.title,
                                color = if (isSelected) Color.White else TextSecondaryDark,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Optional Architecture Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilterChip(
                selected = showArchitecture,
                onClick = { showArchitecture = !showArchitecture },
                label = { Text("System Architecture", fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.AccountTree, contentDescription = null, modifier = Modifier.size(14.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StudioCyanPrimary.copy(alpha = 0.2f),
                    selectedLabelColor = StudioCyanPrimary,
                    containerColor = StudioCardDark,
                    labelColor = TextSecondaryDark
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = showArchitecture,
                    borderColor = if (showArchitecture) StudioCyanPrimary else StudioBorderDark
                ),
                modifier = Modifier.testTag("toggle_architecture_view")
            )
        }

        // Optional Architecture Pipeline
        if (showArchitecture && project != null) {
            Spacer(modifier = Modifier.height(10.dp))
            ArchitectureGraphView(
                platform = project.platform,
                language = project.language,
                securityScore = project.securityScore,
                summary = project.architectureSummary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // File Tabs Horizontal Scroller (when in Code or Split mode)
        if (canvasViewMode != CanvasViewMode.LIVE_PREVIEW && files.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                files.forEachIndexed { index, file ->
                    val isSelected = index == selectedFileIndex
                    Surface(
                        modifier = Modifier
                            .clickable { viewModel.selectFile(index) }
                            .testTag("tab_file_${file.fileName}"),
                        color = if (isSelected) StudioCardHoverDark else StudioCardDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioCyanPrimary else StudioBorderDark
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = null,
                                tint = if (isSelected) StudioCyanPrimary else TextSecondaryDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = file.fileName,
                                color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            Surface(
                                color = StudioCyanPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = file.language,
                                    color = StudioCyanPrimary,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Main Rendered Area: Live Sandbox, Syntax Code, or Both (Split)
        when (canvasViewMode) {
            CanvasViewMode.CODE -> {
                activeFile?.let { file ->
                    SyntaxHighlightedCodeView(
                        code = file.content,
                        language = file.language,
                        fileName = file.fileName
                    )
                } ?: Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No code loaded.", color = TextSecondaryDark)
                }
            }

            CanvasViewMode.LIVE_PREVIEW -> {
                LiveSandboxPreviewView(
                    project = currentProject,
                    isDarkTheme = true
                )
            }

            CanvasViewMode.SPLIT -> {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Live Sandbox Component
                    LiveSandboxPreviewView(
                        project = currentProject,
                        isDarkTheme = true
                    )

                    // Code View Component
                    activeFile?.let { file ->
                        SyntaxHighlightedCodeView(
                            code = file.content,
                            language = file.language,
                            fileName = file.fileName
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Continuous Refinement Chat Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("AUTONOMOUS CODE REFINEMENT", color = StudioCyanPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = refinementText,
                        onValueChange = { refinementText = it },
                        placeholder = { Text("e.g. Add rate-limiting middleware or OAuth flow...", fontSize = 12.sp, color = TextMutedDark) },
                        modifier = Modifier.weight(1f).testTag("input_refinement"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StudioCyanPrimary,
                            unfocusedBorderColor = StudioBorderDark,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    IconButton(
                        onClick = {
                            if (refinementText.isNotBlank()) {
                                viewModel.refineProject(refinementText)
                                refinementText = ""
                            }
                        },
                        modifier = Modifier
                            .background(StudioCyanPrimary, RoundedCornerShape(10.dp))
                            .testTag("btn_send_refinement")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Refine", tint = Color.Black)
                    }
                }
            }
        }
    }
}
