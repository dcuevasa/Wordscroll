package com.wordscroll.domain.settings

import com.wordscroll.core.settings.AppLanguage
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedThemeUseCase @Inject constructor(private val repo: SettingsRepository) {
    operator fun invoke(): Flow<ThemeConfig> = repo.selectedTheme
}

class GetLanguageUseCase @Inject constructor(private val repo: SettingsRepository) {
    operator fun invoke(): Flow<AppLanguage> = repo.language
}

class GetAllThemesUseCase @Inject constructor(private val repo: SettingsRepository) {
    operator fun invoke(): Flow<List<ThemeConfig>> = repo.allThemes
}

class SetLanguageUseCase @Inject constructor(private val repo: SettingsRepository) {
    suspend operator fun invoke(language: AppLanguage) = repo.setLanguage(language)
}

class SelectThemeUseCase @Inject constructor(private val repo: SettingsRepository) {
    suspend operator fun invoke(themeId: String) = repo.selectTheme(themeId)
}

class SaveCustomThemeUseCase @Inject constructor(private val repo: SettingsRepository) {
    suspend operator fun invoke(theme: ThemeConfig) = repo.saveCustomTheme(theme)
}

class DeleteCustomThemeUseCase @Inject constructor(private val repo: SettingsRepository) {
    suspend operator fun invoke(themeId: String) = repo.deleteCustomTheme(themeId)
}
