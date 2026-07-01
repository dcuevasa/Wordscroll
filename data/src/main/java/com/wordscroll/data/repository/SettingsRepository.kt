package com.wordscroll.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wordscroll.core.settings.AppLanguage
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.core.settings.ThemePresets
import com.wordscroll.data.di.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/** Every locally-stored image path a theme may reference (background, companion, ...). */
private fun ThemeConfig.imagePaths(): List<String> = listOfNotNull(customBackgroundPath, companionImagePath)

@Singleton
class SettingsRepository @Inject constructor(
    @SettingsDataStore private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) {
    private val languageKey = stringPreferencesKey("language_tag")
    private val selectedThemeKey = stringPreferencesKey("selected_theme_id")
    private val customThemesKey = stringPreferencesKey("custom_themes")
    private val customType = object : TypeToken<List<ThemeConfig>>() {}.type

    val language: Flow<AppLanguage> = dataStore.data.map {
        AppLanguage.fromTag(it[languageKey])
    }

    val customThemes: Flow<List<ThemeConfig>> = dataStore.data.map {
        decode(it[customThemesKey])
    }

    val allThemes: Flow<List<ThemeConfig>> = customThemes.map { ThemePresets.all + it }

    val selectedTheme: Flow<ThemeConfig> = dataStore.data.map { prefs ->
        val id = prefs[selectedThemeKey] ?: ThemePresets.default.id
        (ThemePresets.all + decode(prefs[customThemesKey])).firstOrNull { it.id == id }
            ?: ThemePresets.default
    }

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { it[languageKey] = language.tag ?: "" }
    }

    suspend fun selectTheme(themeId: String) {
        dataStore.edit { it[selectedThemeKey] = themeId }
    }

    suspend fun saveCustomTheme(theme: ThemeConfig) {
        val previous = customThemes.first().firstOrNull { it.id == theme.id }
        dataStore.edit { prefs ->
            val updated = decode(prefs[customThemesKey]).filterNot { it.id == theme.id } + theme
            prefs[customThemesKey] = gson.toJson(updated)
            prefs[selectedThemeKey] = theme.id
        }
        // Any image no longer referenced by the saved theme (replaced or removed) is orphaned.
        val orphaned = previous?.imagePaths().orEmpty() - theme.imagePaths().toSet()
        orphaned.forEach { deleteImageFileIfUnused(it, excludingThemeId = theme.id) }
    }

    suspend fun deleteCustomTheme(themeId: String) {
        val removed = customThemes.first().firstOrNull { it.id == themeId }
        dataStore.edit { prefs ->
            val updated = decode(prefs[customThemesKey]).filterNot { it.id == themeId }
            prefs[customThemesKey] = gson.toJson(updated)
            if (prefs[selectedThemeKey] == themeId) prefs[selectedThemeKey] = ThemePresets.default.id
        }
        removed?.imagePaths()?.forEach { deleteImageFileIfUnused(it, excludingThemeId = themeId) }
    }

    /** Deletes an image file only if no remaining theme still points to it. */
    private suspend fun deleteImageFileIfUnused(path: String, excludingThemeId: String) {
        val stillUsed = customThemes.first().any { it.id != excludingThemeId && path in it.imagePaths() }
        if (!stillUsed) runCatching { File(path).delete() }
    }

    private fun decode(json: String?): List<ThemeConfig> {
        if (json.isNullOrEmpty()) return emptyList()
        return gson.fromJson<List<ThemeConfig>>(json, customType) ?: emptyList()
    }
}
