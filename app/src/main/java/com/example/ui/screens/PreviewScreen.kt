package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CodifferaLogoBadge
import com.example.ui.preview.LiveSandboxPreviewView
import com.example.ui.theme.*
import com.example.ui.viewmodel.OmniDevViewModel

@Composable
fun PreviewScreen(
    viewModel: OmniDevViewModel,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val project = currentProject?.project

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
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
                    Text("LIVE ISOLATED SANDBOX", color = StudioBlueAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(project?.title ?: "Interactive Runtime", color = TextPrimaryDark, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }

            Surface(
                color = StudioCyanPrimary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, tint = StudioCyanPrimary, modifier = Modifier.size(14.dp))
                    Text(project?.themeStyle ?: "Cyber Executive", color = StudioCyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (currentProject != null) {
            LiveSandboxPreviewView(
                project = currentProject,
                isDarkTheme = true
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No active project loaded. Go to Studio to synthesize a solution.", color = TextSecondaryDark)
                }
            }
        }
    }
}
