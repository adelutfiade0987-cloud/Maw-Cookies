package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF68D391),
    onPrimary = Color(0xFF003822),
    primaryContainer = Color(0xFF005235),
    secondary = Color(0xFFFFB586),
    onSecondary = Color(0xFF522300),
    secondaryContainer = Color(0xFF743400),
    tertiary = Color(0xFFE4BEAE),
    background = Color(0xFF1A1613),
    surface = Color(0xFF26201B),
    onBackground = Color(0xFFECE0D8),
    onSurface = Color(0xFFECE0D8),
    surfaceVariant = Color(0xFF382F28),
    onSurfaceVariant = Color(0xFFD6C4B8),
)

private val LightColorScheme = lightColorScheme(
    primary = CookiePrimary,
    onPrimary = CookieOnPrimary,
    primaryContainer = CookiePillGreen,
    onPrimaryContainer = CookiePillGreenText,
    secondary = CookieSecondary,
    onSecondary = CookieOnSecondary,
    secondaryContainer = CookieSecondaryLight,
    onSecondaryContainer = Color(0xFF652F04),
    tertiary = CookieTertiary,
    onTertiary = Color.White,
    tertiaryContainer = CookieTertiaryLight,
    background = CookieBackground,
    surface = CookieSurface,
    onBackground = CookieTextPrimary,
    onSurface = CookieTextPrimary,
    surfaceVariant = CookieSurfaceVariant,
    onSurfaceVariant = CookieTextSecondary,
    outline = CookieBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
