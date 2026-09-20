package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = StudioCyanPrimary,
    onPrimary = StudioBackgroundDark,
    primaryContainer = StudioCardDark,
    onPrimaryContainer = StudioCyanLight,
    secondary = StudioPurpleSecondary,
    onSecondary = StudioBackgroundDark,
    secondaryContainer = StudioCardHoverDark,
    onSecondaryContainer = StudioVioletAccent,
    tertiary = StudioEmeraldSuccess,
    background = StudioBackgroundDark,
    onBackground = TextPrimaryDark,
    surface = StudioSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = StudioCardDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = StudioBorderDark,
    error = StudioRoseAlert
)

private val LightColorScheme = lightColorScheme(
    primary = StudioPrimaryLight,
    onPrimary = StudioSurfaceLight,
    primaryContainer = StudioCardLight,
    onPrimaryContainer = StudioPrimaryLight,
    secondary = StudioSecondaryLight,
    onSecondary = StudioSurfaceLight,
    secondaryContainer = StudioCardLight,
    onSecondaryContainer = StudioSecondaryLight,
    tertiary = StudioEmeraldSuccess,
    background = StudioBackgroundLight,
    onBackground = TextPrimaryLight,
    surface = StudioSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = StudioCardLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = StudioBorderLight,
    error = StudioRoseAlert
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek dark developer aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

