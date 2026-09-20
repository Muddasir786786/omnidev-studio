package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class ArchNode(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val status: String,
    val detail: String
)

@Composable
fun ArchitectureGraphView(
    platform: String,
    language: String,
    securityScore: Int,
    summary: String,
    modifier: Modifier = Modifier
) {
    var selectedNode by remember { mutableStateOf<ArchNode?>(null) }
    val scrollState = rememberScrollState()

    val nodes = remember(platform, language) {
        listOf(
            ArchNode(
                title = "$platform Client",
                subtitle = "Declarative UI Layer",
                icon = if (platform == "Mobile") Icons.Default.PhoneAndroid else Icons.Default.Language,
                color = StudioCyanPrimary,
                status = "Zero-XSS Sanitized",
                detail = "Built with reactive component hierarchy, client-side input masking, CSP headers, and state flow synchronization."
            ),
            ArchNode(
                title = "Edge API Gateway",
                subtitle = "Rate Limiter & TLS",
                icon = Icons.Default.Router,
                color = StudioPurpleSecondary,
                status = "DDoS Resilient",
                detail = "Terminates TLS 1.3, enforces strict rate limiting (max 100 req/min/IP), validates CORS origins, and inspects request bodies."
            ),
            ArchNode(
                title = "Auth & Security Guard",
                subtitle = "Hardware MFA / JWT",
                icon = Icons.Default.Security,
                color = StudioEmeraldSuccess,
                status = "Argon2id + AES-256",
                detail = "Handles OAuth2 / PKCE authentication flows, validates cryptographic signatures, and isolates tenant contexts."
            ),
            ArchNode(
                title = "$language Engine",
                subtitle = "Domain Business Logic",
                icon = Icons.Default.Memory,
                color = StudioVioletAccent,
                status = "Memory Safe",
                detail = "Decoupled Clean Architecture service layer running type-safe routines with complete error isolation."
            ),
            ArchNode(
                title = "Isolated Data Store",
                subtitle = "Encrypted at Rest",
                icon = Icons.Default.Storage,
                color = StudioCyanLight,
                status = "RLS & Prepared Stmts",
                detail = "Parameterized query execution prevents SQL injection. Row-Level Security ensures absolute tenant isolation."
            )
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(StudioCyanPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AccountTree, contentDescription = null, tint = StudioCyanPrimary, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text("Autonomous Architecture Spec", color = TextPrimaryDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Zero-Trust End-to-End Pipeline", color = TextSecondaryDark, fontSize = 11.sp)
                    }
                }

                Surface(
                    color = StudioEmeraldSuccess.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldSuccess.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(StudioEmeraldSuccess, CircleShape))
                        Text("Score $securityScore/100", color = StudioEmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Architecture Nodes Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                nodes.forEachIndexed { index, node ->
                    val isSelected = selectedNode?.title == node.title

                    Card(
                        modifier = Modifier
                            .width(170.dp)
                            .clickable { selectedNode = if (isSelected) null else node },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) node.color.copy(alpha = 0.18f) else Color(0xFF131D31)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) node.color else StudioBorderDark
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(node.color.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(node.icon, contentDescription = null, tint = node.color, modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = "0${index + 1}",
                                    color = TextMutedDark,
                                    fontSize = 11.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(node.title, color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            Text(node.subtitle, color = TextSecondaryDark, fontSize = 10.sp, maxLines = 1)

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = node.status,
                                color = node.color,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }

                    if (index < nodes.size - 1) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextMutedDark,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Selected Node Details
            selectedNode?.let { node ->
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF0D1424),
                    border = androidx.compose.foundation.BorderStroke(1.dp, node.color.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = node.color, modifier = Modifier.size(14.dp))
                            Text(node.title, color = node.color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(node.detail, color = TextSecondaryDark, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }

            if (summary.isNotBlank() && selectedNode == null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = summary,
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
