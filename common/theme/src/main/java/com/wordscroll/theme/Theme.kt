package com.wordscroll.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = White,
    secondary = PrimaryColor,
    onSecondary = White,
    background = LightBackground,
    onBackground = LightInk,
    surface = LightBackground,
    surfaceTint = LightBackground,
    onSurface = LightInk,
)

private val DarkColors = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = White,
    secondary = PrimaryColor,
    onSecondary = White,
    background = DarkBackground,
    onBackground = DarkInk,
    surface = DarkBackground,
    surfaceTint = DarkBackground,
    onSurface = DarkInk,
)

@Composable
fun WordscrollTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = Typography,
    )
}
