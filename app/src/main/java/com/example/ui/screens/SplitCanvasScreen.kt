package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ArchitectureGraphView
import com.example.ui.components.CodifferaLogoBadge
import com.example.ui.components.SyntaxHighlightedCodeView
import com.example.ui.preview.LiveSandboxPreviewView
import com.example.ui.theme.*
import com.example.ui.viewmodel.CanvasViewMode
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.OmniDevViewModel

@Composable
fun SplitCanvasScreen(
    viewModel: OmniDevViewModel,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val selectedFileIndex by viewModel.selectedFileIndex.collectAsStateWithLifecycle()
    val canvasViewMode by viewModel.canvasViewMode.collectAsStateWithLifecycle()
    val generationState by viewModel.generationState.collectAsStateWithLifecycle()
    val promptInput by viewModel.promptInput.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val configuration = LocalConfiguration.current
    val isExpandedWidth = configuration.screenWidthDp >= 720

    val project = currentProject?.project
    val files = currentProject?.files ?: emptyList()
    val activeFile = files.getOrNull(selectedFileIndex) ?: files.firstOrNull()

    var showArchitecture by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackgroundDark)
    ) {
        // Control Bar: Dual-View Mode Switcher & File Tabs
        Surface(
            color = StudioSurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dual-View Toggle Segmented Button (Code View | Live Preview | Split View)
                    Row(
                        modifier = Modifier
                            .background(StudioBackgroundDark, RoundedCornerShape(8.dp))
                            .border(1.dp, StudioBorderDark, RoundedCornerShape(8.dp))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CanvasViewMode.values().forEach { mode ->
                            val isSelected = canvasViewMode == mode
                            Surface(
                                color = if (isSelected) StudioBlueAccent else Color.Transparent,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .clickable { viewModel.setCanvasViewMode(mode) }
                                    .testTag("btn_mode_${mode.name.lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = when (mode) {
                                            CanvasViewMode.CODE -> Icons.Default.Code
                                            CanvasViewMode.LIVE_PREVIEW -> Icons.Default.Visibility
                                            CanvasViewMode.SPLIT -> Icons.Default.ViewSidebar
                                        },
                                        contentDescription = mode.title,
                                        tint = if (isSelected) Color.White else TextSecondaryDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = mode.title,
                                        color = if (isSelected) Color.White else TextSecondaryDark,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Architecture & Info Actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = showArchitecture,
                            onClick = { showArchitecture = !showArchitecture },
                            label = { Text("Architecture", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.AccountTree, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudioCyanPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = StudioCyanPrimary,
                                containerColor = StudioBackgroundDark,
                                labelColor = TextSecondaryDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = showArchitecture,
                                borderColor = if (showArchitecture) StudioCyanPrimary else StudioBorderDark
                            ),
                            modifier = Modifier.testTag("btn_toggle_architecture")
                        )

                        IconButton(
                            onClick = {
                                activeFile?.let {
                                    clipboardManager.setText(AnnotatedString(it.content))
                                    Toast.makeText(context, "Copied ${it.fileName} to clipboard", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(32.dp).testTag("btn_copy_file_code")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy code", tint = TextSecondaryDark, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // File Tabs Scrollable Row (when files exist)
                if (files.isNotEmpty() && canvasViewMode != CanvasViewMode.LIVE_PREVIEW) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        files.forEachIndexed { idx, file ->
                            val isSelected = idx == selectedFileIndex
                            Surface(
                                color = if (isSelected) StudioCardHoverDark else StudioBackgroundDark,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioCyanPrimary.copy(alpha = 0.6f) else StudioBorderDark
                                ),
                                modifier = Modifier
                                    .clickable { viewModel.selectFile(idx) }
                                    .testTag("tab_file_$idx")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Description,
                                        contentDescription = null,
                                        tint = if (isSelected) StudioCyanPrimary else TextMutedDark,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = file.fileName,
                                        color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                    if (file.isPrimary) {
                                        Box(
                                            modifier = Modifier
                                                .background(StudioCyanPrimary.copy(alpha = 0.2f), RoundedCornerShape(3.dp))
                                                .padding(horizontal = 3.dp, vertical = 1.dp)
                                        ) {
                                            Text("PRIMARY", color = StudioTextCyan, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Optional System Architecture Graph
        AnimatedVisibility(visible = showArchitecture) {
            Column(modifier = Modifier.padding(14.dp)) {
                ArchitectureGraphView(
                    platform = project?.platform ?: "Web",
                    language = project?.language ?: "TypeScript",
                    securityScore = project?.securityScore ?: 98,
                    summary = project?.architectureSummary ?: "Multi-tier zero-trust architecture."
                )
            }
        }

        // Main Body: Split or Single Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (canvasViewMode) {
                CanvasViewMode.CODE -> {
                    // Pure Code View
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(14.dp)
                    ) {
                        activeFile?.let { f ->
                            SyntaxHighlightedCodeView(
                                code = f.content,
                                language = f.language,
                                fileName = f.fileName
                            )
                        } ?: Box(
                            modifier = Modifier.fillMaxSize().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No files loaded. Run a prompt to generate code.", color = TextSecondaryDark)
                        }
                    }
                }

                CanvasViewMode.LIVE_PREVIEW -> {
                    // Pure Live Sandbox Preview
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(14.dp)
                    ) {
                        LiveSandboxPreviewView(
                            project = currentProject,
                            isDarkTheme = true
                        )
                    }
                }

                CanvasViewMode.SPLIT -> {
                    // Responsive Split View (Side-by-side on wide screens, stacked on compact screens)
                    if (isExpandedWidth) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Left pane: Code Editor
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                activeFile?.let { f ->
                                    SyntaxHighlightedCodeView(
                                        code = f.content,
                                        language = f.language,
                                        fileName = f.fileName
                                    )
                                }
                            }

                            // Right pane: Live Sandbox
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                LiveSandboxPreviewView(
                                    project = currentProject,
                                    isDarkTheme = true
                                )
                            }
                        }
                    } else {
                        // Stacked layout with smooth scrolling on mobile
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Live Preview Sandboxed Execution
                            LiveSandboxPreviewView(
                                project = currentProject,
                                isDarkTheme = true
                            )

                            // Syntax-highlighted Code Editor
                            activeFile?.let { f ->
                                SyntaxHighlightedCodeView(
                                    code = f.content,
                                    language = f.language,
                                    fileName = f.fileName
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom AI Studio Prompt & Execution Bar
        Surface(
            color = StudioSurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Generation progress status
                when (val state = generationState) {
                    is GenerationState.Generating -> {
                        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(state.stepMessage, color = StudioCyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text("${(state.progress * 100).toInt()}%", color = StudioTextCyan, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { state.progress },
                                modifier = Modifier.fillMaxWidth().height(4.dp),
                                color = StudioBlueAccent,
                                trackColor = StudioBorderDark
                            )
                        }
                    }
                    is GenerationState.Error -> {
                        Text(
                            text = "⚠ Error: ${state.message}",
                            color = StudioRoseAlert,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                    else -> {}
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextField(
                        value = promptInput,
                        onValueChange = { viewModel.promptInput.value = it },
                        placeholder = {
                            Text("Describe your app, component, or refinement...", color = TextMutedDark, fontSize = 13.sp)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = StudioBackgroundDark,
                            unfocusedContainerColor = StudioBackgroundDark,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 44.dp, max = 80.dp)
                            .border(1.dp, StudioBorderDark, RoundedCornerShape(10.dp))
                            .testTag("input_prompt_split_canvas")
                    )

                    // Run / Generate Button (AI Studio Style)
                    Button(
                        onClick = { viewModel.generateProject() },
                        enabled = generationState !is GenerationState.Generating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudioBlueAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("btn_run_split_canvas")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(18.dp))
                            Text("Run", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
