package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.CanvasViewMode
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.OmniDevViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiStudioHeader(
    viewModel: OmniDevViewModel,
    onOpenSidebar: () -> Unit,
    onNavigateToCanvas: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedModel by viewModel.selectedModel.collectAsStateWithLifecycle()
    val generationState by viewModel.generationState.collectAsStateWithLifecycle()
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val selectedFileIndex by viewModel.selectedFileIndex.collectAsStateWithLifecycle()
    val canvasViewMode by viewModel.canvasViewMode.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showModelMenu by remember { mutableStateOf(false) }

    val models = listOf(
        "Gemini 2.5 Flash",
        "Gemini 1.5 Pro",
        "Claude 3.5 Sonnet",
        "GPT-4o",
        "Codiffera Sandbox"
    )

    val activeFile = currentProject?.files?.getOrNull(selectedFileIndex)
        ?: currentProject?.files?.firstOrNull()

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CodifferaLogoBadge(size = 32.dp, showGlow = true)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Codiff",
                            color = TextPrimaryDark,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "era",
                            color = StudioCyanPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    // Status Indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (generationState is GenerationState.Generating) StudioAmberWarning else StudioEmeraldSuccess)
                        )
                        Text(
                            text = if (generationState is GenerationState.Generating) "Synthesizing..." else "Sandbox Ready",
                            color = if (generationState is GenerationState.Generating) StudioAmberWarning else StudioEmeraldSuccess,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Engine Selector Dropdown Chip
                Box {
                    Surface(
                        color = StudioCardDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark),
                        modifier = Modifier
                            .clickable { showModelMenu = true }
                            .testTag("btn_header_model_dropdown")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = selectedModel,
                                color = StudioBlueAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select engine",
                                tint = StudioBlueAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showModelMenu,
                        onDismissRequest = { showModelMenu = false },
                        modifier = Modifier
                            .background(StudioCardDark)
                            .border(1.dp, StudioBorderDark, RoundedCornerShape(8.dp))
                    ) {
                        models.forEach { model ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = model,
                                        color = if (model == selectedModel) StudioBlueAccent else TextPrimaryDark,
                                        fontWeight = if (model == selectedModel) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                },
                                onClick = {
                                    viewModel.setSelectedModel(model)
                                    showModelMenu = false
                                },
                                leadingIcon = {
                                    if (model == selectedModel) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = StudioBlueAccent, modifier = Modifier.size(16.dp))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onOpenSidebar,
                modifier = Modifier.testTag("btn_open_sidebar")
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimaryDark)
            }
        },
        actions = {
            // Action Buttons: Run, Copy, Clear
            // 1. Clear Button
            IconButton(
                onClick = {
                    viewModel.clearWorkspace()
                    Toast.makeText(context, "Workspace cleared", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(36.dp).testTag("btn_action_clear")
            ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Clear", tint = TextSecondaryDark, modifier = Modifier.size(18.dp))
            }

            // 2. Copy Code Button
            IconButton(
                onClick = {
                    val codeToCopy = activeFile?.content
                        ?: currentProject?.files?.joinToString("\n\n// ===== NEXT FILE =====\n\n") { "// ${it.fileName}\n${it.content}" }
                    if (!codeToCopy.isNullOrBlank()) {
                        clipboardManager.setText(AnnotatedString(codeToCopy))
                        Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No code to copy yet", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(36.dp).testTag("btn_action_copy")
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy code", tint = TextSecondaryDark, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(4.dp))

            // 3. Run Button (Vibrant AI Studio Blue)
            Button(
                onClick = {
                    viewModel.generateProject()
                    onNavigateToCanvas()
                },
                enabled = generationState !is GenerationState.Generating,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StudioBlueAccent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .height(34.dp)
                    .padding(end = 8.dp)
                    .testTag("btn_header_run")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(16.dp))
                    Text("Run", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = StudioSurfaceDark,
            titleContentColor = TextPrimaryDark
        ),
        modifier = modifier.border(
            width = 1.dp,
            color = StudioBorderDark
        )
    )
}
