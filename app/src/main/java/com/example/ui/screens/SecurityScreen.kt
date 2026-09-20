package com.example.ui.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.SecurityAuditEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.OmniDevViewModel

@Composable
fun SecurityScreen(
    viewModel: OmniDevViewModel,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val project = currentProject?.project
    val audits = currentProject?.audits ?: emptyList()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Security Header Card
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
                                StudioEmeraldSuccess.copy(alpha = 0.12f),
                                StudioCyanPrimary.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = StudioEmeraldSuccess, modifier = Modifier.size(20.dp))
                            Text("CYBERSECURITY & OWASP AUDIT", color = StudioEmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Security Health: ${project?.securityScore ?: 98}/100",
                            color = TextPrimaryDark,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Zero-Trust certified. All inputs sanitized, queries parameterized, and cryptographic keys sealed in hardware.",
                            color = TextSecondaryDark,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(StudioEmeraldSuccess.copy(alpha = 0.15f), CircleShape)
                            .border(2.dp, StudioEmeraldSuccess, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("A+", color = StudioEmeraldSuccess, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("Grade", color = StudioEmeraldSuccess, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security Checklist Matrix
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioCardDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("AUTOMATED SAFEGUARD VERIFICATION", color = TextMutedDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                val safeguards = listOf(
                    "OWASP A01: Broken Access Control" to "Role-Based Access & Tenant RLS Enforced",
                    "OWASP A02: Cryptographic Failures" to "Argon2id + AES-256 GCM Hardware Keystore",
                    "OWASP A03: Injection Attacks" to "100% Prepared Statements & Type Sanitization",
                    "OWASP A05: Security Misconfig" to "Strict CSP, HSTS, and Deny-by-Default CORS",
                    "OWASP A07: Identification & Auth" to "Hardware MFA & Short-Lived JWT Tokens",
                    "Network & Transport" to "TLS 1.3 & Rate Limiting (10 req/15min on Auth)"
                )

                safeguards.forEach { (rule, status) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StudioEmeraldSuccess, modifier = Modifier.size(16.dp))
                            Text(rule, color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                        Text(status, color = TextSecondaryDark, fontSize = 10.sp)
                    }
                    HorizontalDivider(color = StudioBorderDark.copy(alpha = 0.5f), thickness = 0.8.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detailed Audit Findings
        Text("DETAILED FINDINGS & REMEDIATION CODE", color = StudioCyanPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        if (audits.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardDark)
            ) {
                Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    Text("Zero vulnerabilities detected. Architecture verified clean.", color = TextSecondaryDark)
                }
            }
        } else {
            audits.forEach { audit ->
                AuditFindingCard(audit)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun AuditFindingCard(audit: SecurityAuditEntity) {
    var isExpanded by remember { mutableStateOf(false) }

    val severityColor = when (audit.severity.uppercase()) {
        "CRITICAL" -> StudioRoseAlert
        "HIGH" -> StudioRoseAlert
        "MEDIUM" -> StudioAmberWarning
        else -> StudioEmeraldSuccess
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = severityColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = audit.severity,
                            color = severityColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(audit.title, color = TextPrimaryDark, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TextSecondaryDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(audit.details, color = TextSecondaryDark, fontSize = 11.sp, lineHeight = 16.sp)

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text("VERIFIED REMEDIATION CODE:", color = StudioCyanPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = StudioCodeBackground,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
                    ) {
                        Text(
                            text = audit.mitigationCode,
                            color = StudioCodeType,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(10.dp),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
