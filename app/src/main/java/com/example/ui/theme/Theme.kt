package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = Color.White,
    primaryContainer = BrandPrimaryContainer,
    onPrimaryContainer = BrandPrimaryDark,
    secondary = BrandPrimaryDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = BrandPrimaryDark,
    tertiary = LogoRedAccent,
    onTertiary = Color.White,
    background = BrandBackground,
    onBackground = TextPrimary,
    surface = BrandSurface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    outline = BrandSurfaceVariant,
    error = StatusError,
    onError = Color.White,
    errorContainer = StatusErrorContainer,
    onErrorContainer = StatusError
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryLight,
    onPrimary = Color.Black,
    primaryContainer = BrandPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = BrandPrimaryLight,
    onSecondary = Color.Black,
    tertiary = LogoRedAccent,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve UZair Gfx brand colors explicitly
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
