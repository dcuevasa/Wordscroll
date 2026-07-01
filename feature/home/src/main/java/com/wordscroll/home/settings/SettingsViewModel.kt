package com.wordscroll.home.settings

import androidx.lifecycle.viewModelScope
import com.wordscroll.core.base.BaseViewModel
import com.wordscroll.core.settings.AppLanguage
import com.wordscroll.core.settings.ThemeConfig
import com.wordscroll.domain.settings.DeleteCustomThemeUseCase
import com.wordscroll.domain.settings.GetAllThemesUseCase
import com.wordscroll.domain.settings.GetLanguageUseCase
import com.wordscroll.domain.settings.GetSelectedThemeUseCase
import com.wordscroll.domain.settings.SaveCustomThemeUseCase
import com.wordscroll.domain.settings.SelectThemeUseCase
import com.wordscroll.domain.settings.SetLanguageUseCase
import com.wordscroll.domain.sources.AddGithubSourceUseCase
import com.wordscroll.domain.sources.GetGithubSourcesUseCase
import com.wordscroll.domain.sources.RemoveGithubSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getLanguageUseCase: GetLanguageUseCase,
    getAllThemesUseCase: GetAllThemesUseCase,
    getSelectedThemeUseCase: GetSelectedThemeUseCase,
    getGithubSourcesUseCase: GetGithubSourcesUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val selectThemeUseCase: SelectThemeUseCase,
    private val saveCustomThemeUseCase: SaveCustomThemeUseCase,
    private val deleteCustomThemeUseCase: DeleteCustomThemeUseCase,
    private val addGithubSourceUseCase: AddGithubSourceUseCase,
    private val removeGithubSourceUseCase: RemoveGithubSourceUseCase,
) : BaseViewModel<ViewState, SettingsEvent>() {

    init {
        viewModelScope.launch {
            combine(
                getLanguageUseCase(),
                getAllThemesUseCase(),
                getSelectedThemeUseCase(),
                getGithubSourcesUseCase()
            ) { language, themes, selected, poemSources ->
                ViewState(
                    language = language,
                    themes = themes,
                    selectedThemeId = selected.id,
                    poemSources = poemSources
                )
            }.collect { updateState(it) }
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { setLanguageUseCase(language) }
    }

    fun selectTheme(themeId: String) {
        viewModelScope.launch { selectThemeUseCase(themeId) }
    }

    fun saveTheme(theme: ThemeConfig) {
        viewModelScope.launch { saveCustomThemeUseCase(theme) }
    }

    fun deleteTheme(themeId: String) {
        viewModelScope.launch { deleteCustomThemeUseCase(themeId) }
    }

    /** Suspends until the repo is validated (or rejected) so the screen can show an inline error. */
    suspend fun addGithubSource(owner: String, repo: String, branch: String): Boolean =
        addGithubSourceUseCase(owner, repo, branch)

    fun removeGithubSource(id: String) {
        viewModelScope.launch { removeGithubSourceUseCase(id) }
    }

    override fun onTriggerEvent(event: SettingsEvent) {}
}
