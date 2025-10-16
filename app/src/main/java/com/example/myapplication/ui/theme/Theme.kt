package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Confident Pink Theme Colors
private val ConfidentPink = Color(0xFFFF4B91) // Main accent
private val SoftPink = Color(0xFFFF8CC6)
private val DeepBackground = Color(0xFF121212)
private val SurfaceDark = Color(0xFF1E1E1E)
private val OnDarkText = Color(0xFFEAEAEA)
private val OnPink = Color(0xFF000000)

private val DarkColorScheme = darkColorScheme(
    primary = ConfidentPink,
    secondary = SoftPink,
    tertiary = ConfidentPink,
    background = DeepBackground,
    surface = SurfaceDark,
    onPrimary = OnPink,
    onSecondary = OnDarkText,
    onTertiary = OnDarkText,
    onBackground = OnDarkText,
    onSurface = OnDarkText
)

@Composable
fun MovieAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
