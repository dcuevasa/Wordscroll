package com.wordscroll.home.settings

import com.wordscroll.core.settings.AppLanguage
import com.wordscroll.core.settings.GithubPoemSource
import com.wordscroll.core.settings.GithubSourceDefaults
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.core.settings.ThemePresets

data class ViewState(
    val language: AppLanguage = AppLanguage.SYSTEM,
    val selectedThemeId: String = ThemePresets.default.id,
    val themes: List<ThemeConfig> = ThemePresets.all,
    val poemSources: List<GithubPoemSource> = GithubSourceDefaults.default,
)

sealed class SettingsEvent
