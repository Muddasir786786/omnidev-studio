package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CodifferaHeroBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.OmniDevViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: OmniDevViewModel,
    onNavigateToCode: () -> Unit,
    onNavigateToPreview: () -> Unit,
    onNavigateToSecurity: () -> Unit
) {
    val promptInput by viewModel.promptInput.collectAsStateWithLifecycle()
    val selectedPlatform by viewModel.selectedPlatform.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedFramework by viewModel.selectedFramework.collectAsStateWithLifecycle()
    val selectedStyle by viewModel.selectedStyle.collectAsStateWithLifecycle()
    val generationState by viewModel.generationState.collectAsStateWithLifecycle()
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    val quickPrompts = listOf(
        "Full-Stack SaaS with Stripe & OAuth" to ("Web" to "TypeScript"),
        "FinTech Crypto Wallet with Biometric HSM" to ("Mobile" to "Kotlin"),
        "High-Speed Async Microservice in Rust" to ("Backend" to "Rust"),
        "AI Computer Vision Inference API" to ("Backend" to "Python"),
        "Zero-Trust Linux Hardening & CI/CD" to ("DevOps" to "Bash"),
        "SwiftUI iOS Health Tracker" to ("Mobile" to "Swift"),
        "Relational Schema with Immutable Audit" to ("Backend" to "SQL")
    )

    val platforms = listOf("Web", "Mobile", "Backend", "Fullstack", "Systems", "DevOps")
    val languages = listOf("TypeScript", "Kotlin", "Python", "Rust", "Go", "Swift", "C++", "SQL", "Bash", "PHP")
    val styles = listOf("Cyber Executive", "Modern Minimalist", "Glassmorphic", "Bento Grid")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Prominent Codiffera Brand Header with glowing startup splash effect
        CodifferaHeroBanner(
            subtitle = "DEV TOOL STUDIO",
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // Hero Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                StudioCyanPrimary.copy(alpha = 0.12f),
                                StudioPurpleSecondary.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        Brush.linearGradient(listOf(StudioCyanPrimary, StudioPurpleSecondary)),
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = Color.Black, modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text(
                                    text = "AUTONOMOUS AI DEV & DESIGNER",
                                    color = StudioCyanPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Full-Stack Code Synthesis",
                                    color = TextPrimaryDark,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            color = StudioEmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldSuccess.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(modifier = Modifier.size(7.dp).background(StudioEmeraldSuccess, CircleShape))
                                Text("Engine Ready", color = StudioEmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Describe any mobile app or website. Codiffera autonomously architects the system, designs the UI/UX tokens, produces multi-file functional code, and enforces OWASP security hardening.",
                        color = TextSecondaryDark,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prompt Input Field
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "NATURAL LANGUAGE SPECIFICATION",
                    color = TextMutedDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = promptInput,
                    onValueChange = { viewModel.promptInput.value = it },
                    placeholder = {
                        Text(
                            "e.g. Build a secure real-time collaboration canvas with WebSockets, end-to-end encryption, and role-based access control...",
                            color = TextMutedDark,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                        .testTag("input_prompt"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0D1424),
                        unfocusedContainerColor = Color(0xFF0D1424),
                        focusedBorderColor = StudioCyanPrimary,
                        unfocusedBorderColor = StudioBorderDark,
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Prompt Inspiration Chips
                Text("Quick Architecture Starters:", color = TextSecondaryDark, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quickPrompts) { (label, config) ->
                        Surface(
                            modifier = Modifier.clickable {
                                viewModel.promptInput.value = label
                                viewModel.setPlatform(config.first)
                                viewModel.setLanguage(config.second)
                            },
                            color = Color(0xFF162136),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = StudioAmberWarning, modifier = Modifier.size(13.dp))
                                Text(label, color = TextPrimaryDark, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Target Stack & Design Selectors
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Platform selector
                Text("1. TARGET PLATFORM", color = TextMutedDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    platforms.forEach { plat ->
                        val isSel = selectedPlatform == plat
                        FilterChip(
                            selected = isSel,
                            onClick = { viewModel.setPlatform(plat) },
                            label = { Text(plat, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudioCyanPrimary,
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF131D31),
                                labelColor = TextPrimaryDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSel,
                                borderColor = StudioBorderDark,
                                selectedBorderColor = StudioCyanPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Language selector
                Text("2. PROGRAMMING LANGUAGE", color = TextMutedDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    languages.forEach { lang ->
                        val isSel = selectedLanguage == lang
                        FilterChip(
                            selected = isSel,
                            onClick = { viewModel.setLanguage(lang) },
                            label = { Text(lang, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudioPurpleSecondary,
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF131D31),
                                labelColor = TextPrimaryDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSel,
                                borderColor = StudioBorderDark,
                                selectedBorderColor = StudioPurpleSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // UI/UX Aesthetic Style
                Text("3. UI/UX DESIGN SYSTEM", color = TextMutedDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    styles.forEach { style ->
                        val isSel = selectedStyle == style
                        FilterChip(
                            selected = isSel,
                            onClick = { viewModel.selectedStyle.value = style },
                            label = { Text(style, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudioEmeraldSuccess,
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF131D31),
                                labelColor = TextPrimaryDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSel,
                                borderColor = StudioBorderDark,
                                selectedBorderColor = StudioEmeraldSuccess
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Autonomous Pipeline Progress Tracker
        if (generationState is GenerationState.Generating) {
            val gen = generationState as GenerationState.Generating
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyanPrimary)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = StudioCyanPrimary,
                                strokeWidth = 2.dp
                            )
                            Text("AUTONOMOUS AGENT ACTIVE", color = StudioCyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("${(gen.progress * 100).toInt()}%", color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { gen.progress },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = StudioCyanPrimary,
                        trackColor = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(gen.stepMessage, color = TextSecondaryDark, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Primary Synthesis Button
        Button(
            onClick = { viewModel.generateProject() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("btn_synthesize_project"),
            colors = ButtonDefaults.buttonColors(containerColor = StudioCyanPrimary),
            shape = RoundedCornerShape(14.dp),
            enabled = generationState !is GenerationState.Generating
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Default.Terminal, contentDescription = null, tint = Color.Black)
                Text(
                    text = "Synthesize Complete Solution",
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Current Active Project Snapshot Card
        currentProject?.let { full ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ACTIVE WORKSPACE",
                                color = StudioCyanPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = full.project.title,
                                color = TextPrimaryDark,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = StudioEmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Security ${full.project.securityScore}/100",
                                color = StudioEmeraldSuccess,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = full.project.description,
                        color = TextSecondaryDark,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Jump Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToCode,
                            modifier = Modifier.weight(1f).testTag("btn_goto_code"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = StudioCyanPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Code (${full.files.size})", color = TextPrimaryDark, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onNavigateToPreview,
                            modifier = Modifier.weight(1f).testTag("btn_goto_preview"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = StudioPurpleSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Preview", color = TextPrimaryDark, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onNavigateToSecurity,
                            modifier = Modifier.weight(1f).testTag("btn_goto_security"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = StudioEmeraldSuccess, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Security", color = TextPrimaryDark, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
