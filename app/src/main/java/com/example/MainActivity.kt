package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AiStudioHeader
import com.example.ui.components.AiStudioSidebarContent
import com.example.ui.components.StudioDestination
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CanvasViewMode
import com.example.ui.viewmodel.OmniDevViewModel
import kotlinx.coroutines.launch

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
    var selectedDestination by remember { mutableStateOf(StudioDestination.STUDIO) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = StudioSurfaceDark,
                modifier = Modifier.width(320.dp)
            ) {
                AiStudioSidebarContent(
                    viewModel = viewModel,
                    currentDestination = selectedDestination,
                    onSelectDestination = { dest ->
                        selectedDestination = dest
                    },
                    onCloseSidebar = {
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = StudioBackgroundDark,
            topBar = {
                AiStudioHeader(
                    viewModel = viewModel,
                    onOpenSidebar = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    onNavigateToCanvas = {
                        selectedDestination = StudioDestination.SPLIT_CANVAS
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = StudioSurfaceDark,
                    tonalElevation = 4.dp
                ) {
                    StudioDestination.values().forEach { dest ->
                        val isSelected = selectedDestination == dest
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedDestination = dest },
                            icon = {
                                Icon(
                                    imageVector = dest.icon,
                                    contentDescription = dest.title,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = dest.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = StudioBlueAccent,
                                indicatorColor = StudioBlueAccent,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            ),
                            modifier = Modifier.testTag("nav_${dest.name.lowercase()}")
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
                Crossfade(targetState = selectedDestination, label = "ScreenTransition") { dest ->
                    when (dest) {
                        StudioDestination.STUDIO -> StudioScreen(
                            viewModel = viewModel,
                            onNavigateToCode = {
                                viewModel.setCanvasViewMode(CanvasViewMode.CODE)
                                selectedDestination = StudioDestination.SPLIT_CANVAS
                            },
                            onNavigateToPreview = {
                                viewModel.setCanvasViewMode(CanvasViewMode.LIVE_PREVIEW)
                                selectedDestination = StudioDestination.SPLIT_CANVAS
                            },
                            onNavigateToSecurity = {
                                selectedDestination = StudioDestination.SECURITY
                            }
                        )
                        StudioDestination.SPLIT_CANVAS -> SplitCanvasScreen(
                            viewModel = viewModel
                        )
                        StudioDestination.PREVIEW -> PreviewScreen(
                            viewModel = viewModel
                        )
                        StudioDestination.SECURITY -> SecurityScreen(
                            viewModel = viewModel
                        )
                        StudioDestination.PROJECTS -> ProjectsScreen(
                            viewModel = viewModel,
                            onProjectLoaded = {
                                selectedDestination = StudioDestination.SPLIT_CANVAS
                            }
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
}
