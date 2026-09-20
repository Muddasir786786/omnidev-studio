package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.OmniDevViewModel

enum class OmniNavTab(val title: String, val icon: ImageVector) {
    STUDIO("Studio", Icons.Default.AutoFixHigh),
    CODE("Code", Icons.Default.Code),
    PREVIEW("Preview", Icons.Default.Visibility),
    SECURITY("Security", Icons.Default.Shield),
    PROJECTS("Projects", Icons.Default.Folder)
}

class MainActivity : ComponentActivity() {
    private val viewModel: OmniDevViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isAppDarkTheme.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                OmniDevAppRoot(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OmniDevAppRoot(viewModel: OmniDevViewModel) {
    var selectedTab by remember { mutableStateOf(OmniNavTab.STUDIO) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    val project = currentProject?.project

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = StudioBackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    Brush.linearGradient(listOf(StudioCyanPrimary, StudioPurpleSecondary)),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Terminal, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text(
                                text = "OmniDev Studio",
                                color = TextPrimaryDark,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            project?.let { p ->
                                Text(
                                    text = "${p.platform} • ${p.language}",
                                    color = StudioCyanPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.testTag("btn_open_settings")
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Settings", tint = TextSecondaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StudioSurfaceDark,
                    titleContentColor = TextPrimaryDark
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = StudioSurfaceDark,
                tonalElevation = 4.dp
            ) {
                OmniNavTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = StudioCyanPrimary,
                            indicatorColor = StudioCyanPrimary,
                            unselectedIconColor = TextSecondaryDark,
                            unselectedTextColor = TextSecondaryDark
                        ),
                        modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = selectedTab, label = "ScreenTransition") { tab ->
                when (tab) {
                    OmniNavTab.STUDIO -> StudioScreen(
                        viewModel = viewModel,
                        onNavigateToCode = { selectedTab = OmniNavTab.CODE },
                        onNavigateToPreview = { selectedTab = OmniNavTab.PREVIEW },
                        onNavigateToSecurity = { selectedTab = OmniNavTab.SECURITY }
                    )
                    OmniNavTab.CODE -> CodeExplorerScreen(viewModel = viewModel)
                    OmniNavTab.PREVIEW -> PreviewScreen(viewModel = viewModel)
                    OmniNavTab.SECURITY -> SecurityScreen(viewModel = viewModel)
                    OmniNavTab.PROJECTS -> ProjectsScreen(
                        viewModel = viewModel,
                        onProjectLoaded = { selectedTab = OmniNavTab.CODE }
                    )
                }
            }
        }

        if (showSettingsDialog) {
            SettingsDialog(
                viewModel = viewModel,
                onDismiss = { showSettingsDialog = false }
            )
        }
    }
}
