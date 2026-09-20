package com.example.ui.preview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.FullProject
import com.example.ui.theme.*

enum class PreviewViewport(val label: String, val maxWidthDp: Int?) {
    MOBILE("Mobile", 375),
    TABLET("Tablet", 680),
    DESKTOP("Full", null)
}

data class ConsoleLogItem(
    val level: String,
    val message: String,
    val timestamp: String = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LiveSandboxPreviewView(
    project: FullProject?,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true
) {
    var viewport by remember { mutableStateOf(PreviewViewport.DESKTOP) }
    var showConsoleLogs by remember { mutableStateOf(false) }
    var reloadTrigger by remember { mutableIntStateOf(0) }
    val consoleLogs = remember { mutableStateListOf<ConsoleLogItem>() }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isSandboxReady by remember { mutableStateOf(false) }

    // Synthesize HTML whenever project or reload trigger changes
    val renderedHtml = remember(project, reloadTrigger, isDarkTheme) {
        SandboxPreviewEngine.buildSandboxHtml(project = project, isDarkMode = isDarkTheme)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderDark)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Preview Header Bar (Google AI Studio / DevTools style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StudioSurfaceDark)
                    .border(
                        width = 1.dp,
                        color = StudioBorderDark,
                        shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isSandboxReady) StudioEmeraldSuccess else StudioCyanPrimary)
                    )
                    Text(
                        text = "LIVE SANDBOX",
                        color = TextPrimaryDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Surface(
                        color = StudioCyanPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "SECURE ISOLATED DOM",
                            color = StudioTextCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Controls: Viewport, Reload, Console Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Viewport Buttons
                    PreviewViewport.values().forEach { vp ->
                        val isSelected = viewport == vp
                        IconButton(
                            onClick = { viewport = vp },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_viewport_${vp.name.lowercase()}")
                        ) {
                            Icon(
                                imageVector = when (vp) {
                                    PreviewViewport.MOBILE -> Icons.Default.PhoneIphone
                                    PreviewViewport.TABLET -> Icons.Default.TabletMac
                                    PreviewViewport.DESKTOP -> Icons.Default.DesktopWindows
                                },
                                contentDescription = vp.label,
                                tint = if (isSelected) StudioBlueAccent else TextSecondaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Reload Button
                    IconButton(
                        onClick = {
                            reloadTrigger++
                            consoleLogs.add(ConsoleLogItem("INFO", "Manual sandbox hot reload triggered"))
                        },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("btn_reload_sandbox")
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Reload Sandbox",
                            tint = StudioCyanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Console Log Toggle
                    IconButton(
                        onClick = { showConsoleLogs = !showConsoleLogs },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("btn_toggle_console")
                    ) {
                        BadgedBox(
                            badge = {
                                if (consoleLogs.isNotEmpty()) {
                                    Badge(containerColor = StudioBlueAccent) {
                                        Text("${consoleLogs.size.coerceAtMost(99)}", fontSize = 8.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Terminal,
                                contentDescription = "Console Logs",
                                tint = if (showConsoleLogs) StudioBlueAccent else TextSecondaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Sandbox Viewport Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(460.dp)
                    .background(Color(0xFF07090D)),
                contentAlignment = Alignment.TopCenter
            ) {
                val maxW = viewport.maxWidthDp
                val viewportModifier = if (maxW != null) {
                    Modifier
                        .width(maxW.dp)
                        .fillMaxHeight()
                        .padding(vertical = 8.dp)
                        .border(1.dp, StudioBorderDark, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                } else {
                    Modifier.fillMaxSize()
                }

                AndroidView(
                    modifier = viewportModifier.testTag("live_sandbox_webview"),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                allowFileAccess = false
                                allowContentAccess = false
                                builtInZoomControls = false
                                displayZoomControls = false
                                databaseEnabled = true
                                cacheMode = WebSettings.LOAD_NO_CACHE
                            }

                            webChromeClient = object : WebChromeClient() {
                                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                    consoleMessage?.let {
                                        consoleLogs.add(
                                            ConsoleLogItem(
                                                level = it.messageLevel().name,
                                                message = "${it.message()} (line ${it.lineNumber()})"
                                            )
                                        )
                                    }
                                    return true
                                }
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    isSandboxReady = false
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isSandboxReady = true
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    // Keep all clicks strictly inside sandbox
                                    return true
                                }
                            }

                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            webViewRef = this
                        }
                    },
                    update = { webView ->
                        webViewRef = webView
                        webView.loadDataWithBaseURL(
                            "https://codiffera-sandbox.local/",
                            renderedHtml,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                )
            }

            // Expandable Console Telemetry Drawer
            AnimatedVisibility(visible = showConsoleLogs) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0B0E14))
                        .border(1.dp, StudioBorderDark)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE JAVASCRIPT CONSOLE & TELEMETRY",
                            color = StudioTextCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        TextButton(
                            onClick = { consoleLogs.clear() },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(26.dp)
                        ) {
                            Text("Clear", color = TextSecondaryDark, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (consoleLogs.isEmpty()) {
                        Text(
                            text = "No console warnings or messages logged yet. Sandbox execution is clean.",
                            color = TextMutedDark,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 140.dp)
                        ) {
                            items(consoleLogs.toList()) { log ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "[${log.timestamp}]",
                                        color = TextMutedDark,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    val logColor = when (log.level) {
                                        "ERROR" -> StudioRoseAlert
                                        "WARNING" -> StudioAmberWarning
                                        else -> StudioCyanLight
                                    }
                                    Text(
                                        text = log.level,
                                        color = logColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = log.message,
                                        color = TextPrimaryDark,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
