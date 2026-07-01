package com.wordscroll.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.core.settings.ThemePresets

@Composable
fun WordscrollTheme(
    config: ThemeConfig = ThemePresets.default,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppTheme provides config) {
        MaterialTheme(
            colorScheme = config.colorScheme(),
            typography = config.typography(),
            content = content,
        )
    }
}
