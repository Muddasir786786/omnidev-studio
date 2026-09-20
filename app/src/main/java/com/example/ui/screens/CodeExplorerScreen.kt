package com.example.ui.screens

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
import com.example.ui.components.SyntaxHighlightedCodeView
import com.example.ui.theme.*
import com.example.ui.viewmodel.OmniDevViewModel

@Composable
fun CodeExplorerScreen(
    viewModel: OmniDevViewModel,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val selectedFileIndex by viewModel.selectedFileIndex.collectAsStateWithLifecycle()
    var showArchitecture by remember { mutableStateOf(false) }
    var refinementText by remember { mutableStateOf("") }

    val project = currentProject?.project
    val files = currentProject?.files ?: emptyList()
    val activeFile = files.getOrNull(selectedFileIndex) ?: files.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("CODE & SYSTEM ARCHITECTURE", color = StudioCyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(project?.title ?: "Project Code", color = TextPrimaryDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = showArchitecture,
                    onClick = { showArchitecture = !showArchitecture },
                    label = { Text("Architecture") },
                    leadingIcon = { Icon(Icons.Default.AccountTree, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioCyanPrimary,
                        selectedLabelColor = Color.Black
                    ),
                    modifier = Modifier.testTag("toggle_architecture_view")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Optional Architecture Pipeline
        if (showArchitecture && project != null) {
            ArchitectureGraphView(
                platform = project.platform,
                language = project.language,
                securityScore = project.securityScore,
                summary = project.architectureSummary
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // File Tabs Horizontal Scroller
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
                    color = if (isSelected) StudioCardHoverDark else Color(0xFF131D31),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioCyanPrimary else StudioBorderDark
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
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

        // File Metadata Bar
        activeFile?.let { file ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(file.fileType, color = StudioVioletAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text("•", color = TextMutedDark)
                    Text("${file.content.lines().size} lines", color = TextSecondaryDark, fontSize = 11.sp)
                    Text("•", color = TextMutedDark)
                    Text("${file.content.length} bytes", color = TextSecondaryDark, fontSize = 11.sp)
                }

                Surface(
                    color = StudioEmeraldSuccess.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "OWASP Hardened",
                        color = StudioEmeraldSuccess,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Syntax Highlighted Code Viewer
            SyntaxHighlightedCodeView(
                code = file.content,
                language = file.language,
                fileName = file.fileName
            )
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
                            focusedContainerColor = Color(0xFF0D1424),
                            unfocusedContainerColor = Color(0xFF0D1424),
                            focusedBorderColor = StudioCyanPrimary,
                            unfocusedBorderColor = StudioBorderDark,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (refinementText.isNotBlank()) {
                                viewModel.refineProject(refinementText)
                                refinementText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyanPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_apply_refinement")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Apply", tint = Color.Black, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
