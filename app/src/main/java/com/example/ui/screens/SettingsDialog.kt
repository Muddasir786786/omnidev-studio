package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.ui.theme.*
import com.example.ui.viewmodel.OmniDevViewModel

@Composable
fun SettingsDialog(
    viewModel: OmniDevViewModel,
    onDismiss: () -> Unit
) {
    val customApiKey by viewModel.customApiKey.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isAppDarkTheme.collectAsStateWithLifecycle()
    var tempKey by remember { mutableStateOf(customApiKey) }

    val hasBuildConfigKey = remember {
        try {
            BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"
        } catch (e: Throwable) {
            false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    viewModel.customApiKey.value = tempKey
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = StudioCyanPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Settings", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondaryDark)
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Key, contentDescription = null, tint = StudioCyanPrimary)
                Text("OmniDev Engine Settings", color = TextPrimaryDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("GEMINI AI ENGINE CONFIGURATION", color = StudioCyanPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (hasBuildConfigKey)
                        "Platform Secret: Gemini API Key injected via AI Studio Secrets."
                    else
                        "Configure a custom Gemini API key or use the built-in autonomous blueprint synthesis engine.",
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = tempKey,
                    onValueChange = { tempKey = it },
                    placeholder = {
                        Text(if (hasBuildConfigKey) "Active (BuildConfig injected)" else "Enter Gemini API Key...", fontSize = 12.sp, color = TextMutedDark)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("input_custom_api_key"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0D1424),
                        unfocusedContainerColor = Color(0xFF0D1424),
                        focusedBorderColor = StudioCyanPrimary,
                        unfocusedBorderColor = StudioBorderDark,
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("REASONING & MODEL SPEC", color = TextMutedDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(0xFF131D31),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Model: gemini-3.5-flash", color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
                        Text("Endpoint: v1beta REST API / Direct Autonomous Engine", color = TextSecondaryDark, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Theme Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Developer Dark Mode", color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { viewModel.isAppDarkTheme.value = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = StudioCyanPrimary,
                            checkedTrackColor = StudioCardDark
                        )
                    )
                }
            }
        },
        containerColor = StudioSurfaceDark,
        shape = RoundedCornerShape(16.dp)
    )
}
