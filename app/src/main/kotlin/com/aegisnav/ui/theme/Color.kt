package com.aegisnav.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Black/Grey/White Color Palette
object AegisColors {
    // Primary - Black shades
    val Black = Color(0xFF000000)
    val BlackPrimary = Color(0xFF1a1a1a)
    val Black800 = Color(0xFF2d2d2d)
    val Black700 = Color(0xFF3f3f3f)
    
    // Grey Scale
    val Grey600 = Color(0xFF4f4f4f)
    val Grey500 = Color(0xFF808080)
    val Grey400 = Color(0xFFb0b0b0)
    val Grey300 = Color(0xFFd0d0d0)
    val Grey200 = Color(0xFFe8e8e8)
    val Grey100 = Color(0xFFf5f5f5)
    
    // White
    val White = Color(0xFFffffff)
    
    // Functional Colors (muted)
    val SuccessGreen = Color(0xFF2d5016)
    val WarningOrange = Color(0xFF5d3a1a)
    val ErrorRed = Color(0xFF5d1a1a)
    val InfoBlue = Color(0xFF1a3d5d)
    
    // Background
    val BgDark = Color(0xFF0d0d0d)
    val BgSurface = Color(0xFF1e1e1e)
    val BgLight = Color(0xFFf9f9f9)
    
    // Text
    val TextPrimary = Color(0xFFffffff)
    val TextSecondary = Color(0xFFb0b0b0)
    val TextTertiary = Color(0xFF808080)
    val TextDisabled = Color(0xFF505050)
    
    // Dividers
    val Divider = Color(0xFF333333)
    val Border = Color(0xFF404040)
}

private val darkColorScheme = darkColorScheme(
    primary = AegisColors.Grey600,
    onPrimary = AegisColors.White,
    primaryContainer = AegisColors.Black700,
    onPrimaryContainer = AegisColors.White,
    secondary = AegisColors.Grey500,
    onSecondary = AegisColors.White,
    secondaryContainer = AegisColors.Black800,
    onSecondaryContainer = AegisColors.White,
    tertiary = AegisColors.Grey400,
    onTertiary = AegisColors.Black,
    tertiaryContainer = AegisColors.Black700,
    onTertiaryContainer = AegisColors.White,
    error = AegisColors.ErrorRed,
    onError = AegisColors.White,
    errorContainer = AegisColors.ErrorRed,
    onErrorContainer = AegisColors.White,
    background = AegisColors.BgDark,
    onBackground = AegisColors.TextPrimary,
    surface = AegisColors.BgSurface,
    onSurface = AegisColors.TextPrimary,
    surfaceVariant = AegisColors.Black800,
    onSurfaceVariant = AegisColors.TextSecondary,
    outline = AegisColors.Divider,
    outlineVariant = AegisColors.Border,
    scrim = Color.Black.copy(alpha = 0.5f)
)

private val lightColorScheme = lightColorScheme(
    primary = AegisColors.Black700,
    onPrimary = AegisColors.White,
    primaryContainer = AegisColors.Grey200,
    onPrimaryContainer = AegisColors.Black,
    secondary = AegisColors.Grey500,
    onSecondary = AegisColors.White,
    secondaryContainer = AegisColors.Grey100,
    onSecondaryContainer = AegisColors.Black,
    tertiary = AegisColors.Grey600,
    onTertiary = AegisColors.White,
    tertiaryContainer = AegisColors.Grey200,
    onTertiaryContainer = AegisColors.Black,
    error = AegisColors.ErrorRed,
    onError = AegisColors.White,
    errorContainer = AegisColors.ErrorRed,
    onErrorContainer = AegisColors.White,
    background = AegisColors.BgLight,
    onBackground = AegisColors.Black,
    surface = AegisColors.White,
    onSurface = AegisColors.Black,
    surfaceVariant = AegisColors.Grey200,
    onSurfaceVariant = AegisColors.Grey600,
    outline = AegisColors.Grey400,
    outlineVariant = AegisColors.Grey300,
    scrim = Color.Black.copy(alpha = 0.5f)
)

@Composable
fun AegisNavTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AegisTypography,
        content = content
    )
}
