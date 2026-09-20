package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class ViewportMode {
    MOBILE, TABLET, DESKTOP
}

@Composable
fun InteractivePreviewCanvas(
    title: String,
    platform: String,
    language: String,
    framework: String,
    themeStyle: String,
    modifier: Modifier = Modifier
) {
    var viewportMode by remember { mutableStateOf(ViewportMode.MOBILE) }
    var isDarkPreview by remember { mutableStateOf(true) }
    var showTokens by remember { mutableStateOf(false) }

    // Simulated interactive canvas states
    var counter by remember { mutableIntStateOf(142) }
    var notificationMessage by remember { mutableStateOf<String?>(null) }
    var activeTab by remember { mutableStateOf("Overview") }

    val canvasBackground = if (isDarkPreview) Color(0xFF0F172A) else Color(0xFFF8FAFC)
    val canvasCard = if (isDarkPreview) Color(0xFF1E293B) else Color(0xFFFFFFFF)
    val canvasTextPrimary = if (isDarkPreview) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val canvasTextSecondary = if (isDarkPreview) Color(0xFF94A3B8) else Color(0xFF64748B)
    val canvasBorder = if (isDarkPreview) Color(0xFF334155) else Color(0xFFE2E8F0)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
    ) {
        Column {
            // Viewport & Toolbar Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B22))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Viewport selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { viewportMode = ViewportMode.MOBILE },
                        modifier = Modifier.size(32.dp).testTag("btn_viewport_mobile")
                    ) {
                        Icon(
                            Icons.Default.PhoneIphone,
                            contentDescription = "Mobile Viewport",
                            tint = if (viewportMode == ViewportMode.MOBILE) StudioCyanPrimary else TextSecondaryDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { viewportMode = ViewportMode.TABLET },
                        modifier = Modifier.size(32.dp).testTag("btn_viewport_tablet")
                    ) {
                        Icon(
                            Icons.Default.TabletMac,
                            contentDescription = "Tablet Viewport",
                            tint = if (viewportMode == ViewportMode.TABLET) StudioCyanPrimary else TextSecondaryDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { viewportMode = ViewportMode.DESKTOP },
                        modifier = Modifier.size(32.dp).testTag("btn_viewport_desktop")
                    ) {
                        Icon(
                            Icons.Default.Laptop,
                            contentDescription = "Desktop Viewport",
                            tint = if (viewportMode == ViewportMode.DESKTOP) StudioCyanPrimary else TextSecondaryDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // URL / Device Bar simulation
                Surface(
                    color = Color(0xFF0D1117),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(StudioEmeraldSuccess, CircleShape))
                        Text(
                            text = "https://${title.lowercase().replace(" ", "-").take(16)}.preview.app",
                            color = TextSecondaryDark,
                            fontSize = 11.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }

                // Theme & Tokens Toggles
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { isDarkPreview = !isDarkPreview },
                        modifier = Modifier.size(32.dp).testTag("btn_theme_toggle")
                    ) {
                        Icon(
                            imageVector = if (isDarkPreview) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Preview Theme",
                            tint = StudioAmberWarning,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { showTokens = !showTokens },
                        modifier = Modifier.size(32.dp).testTag("btn_tokens_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Design Tokens",
                            tint = if (showTokens) StudioPurpleSecondary else TextSecondaryDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Design Tokens Sheet Drawer
            AnimatedVisibility(visible = showTokens) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F1524))
                        .padding(14.dp)
                ) {
                    Text("AUTONOMOUS UI/UX DESIGN SYSTEM", color = StudioCyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TokenSwatch("Primary", StudioCyanPrimary)
                        TokenSwatch("Secondary", StudioPurpleSecondary)
                        TokenSwatch("Surface", canvasCard)
                        TokenSwatch("Success", StudioEmeraldSuccess)
                        TokenSwatch("Border", canvasBorder)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Typography: Plus Jakarta Sans / Inter | Grid: 8dp standard | Contrast: 8.4:1 AAA Compliant",
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )
                }
            }

            // Canvas Workspace Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 380.dp, max = 500.dp)
                    .background(Color(0xFF0B0F19))
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Adaptive Device Frame
                val frameModifier = when (viewportMode) {
                    ViewportMode.MOBILE -> Modifier.width(320.dp)
                    ViewportMode.TABLET -> Modifier.width(420.dp)
                    ViewportMode.DESKTOP -> Modifier.fillMaxWidth()
                }

                Card(
                    modifier = frameModifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = canvasBackground),
                    border = androidx.compose.foundation.BorderStroke(1.dp, canvasBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // In-Preview App Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(StudioCyanPrimary, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                }
                                Text(title.take(18), color = canvasTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Surface(
                                color = StudioEmeraldSuccess.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "LIVE",
                                    color = StudioEmeraldSuccess,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // In-Preview Navigation Tabs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Overview", "Security", "Logs").forEach { tab ->
                                val isSelected = activeTab == tab
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (isSelected) StudioCyanPrimary else canvasCard,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) StudioCyanPrimary else canvasBorder,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { activeTab = tab }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = tab,
                                        color = if (isSelected) Color.Black else canvasTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interactive Hero Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = canvasCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, canvasBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "AUTONOMOUS SYSTEM TELEMETRY",
                                    color = StudioCyanPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "$counter Transactions",
                                            color = canvasTextPrimary,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Latency: 8.2ms | 100% Uptime",
                                            color = canvasTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            counter += 1
                                            notificationMessage = "Simulated Request #$counter dispatched safely!"
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyanPrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Simulate", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Simulated Toast Message
                        notificationMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = StudioEmeraldSuccess.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldSuccess.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StudioEmeraldSuccess, modifier = Modifier.size(14.dp))
                                    Text(msg, color = StudioEmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Features Grid inside preview
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = canvasCard),
                                border = androidx.compose.foundation.BorderStroke(1.dp, canvasBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = StudioEmeraldSuccess, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Zero-Trust", color = canvasTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("AES-256 GCM", color = canvasTextSecondary, fontSize = 9.sp)
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = canvasCard),
                                border = androidx.compose.foundation.BorderStroke(1.dp, canvasBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Icon(Icons.Default.Speed, contentDescription = null, tint = StudioCyanPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Ultra Fast", color = canvasTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Optimized VM", color = canvasTextSecondary, fontSize = 9.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TokenSwatch(name: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color, RoundedCornerShape(6.dp))
                .border(1.dp, StudioBorderDark, RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(name, color = TextMutedDark, fontSize = 9.sp)
    }
}
