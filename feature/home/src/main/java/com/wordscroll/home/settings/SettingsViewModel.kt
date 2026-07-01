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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getLanguageUseCase: GetLanguageUseCase,
    getAllThemesUseCase: GetAllThemesUseCase,
    getSelectedThemeUseCase: GetSelectedThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val selectThemeUseCase: SelectThemeUseCase,
    private val saveCustomThemeUseCase: SaveCustomThemeUseCase,
    private val deleteCustomThemeUseCase: DeleteCustomThemeUseCase,
) : BaseViewModel<ViewState, SettingsEvent>() {

    init {
        viewModelScope.launch {
            combine(
                getLanguageUseCase(),
                getAllThemesUseCase(),
                getSelectedThemeUseCase()
            ) { language, themes, selected ->
                ViewState(language = language, themes = themes, selectedThemeId = selected.id)
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

    override fun onTriggerEvent(event: SettingsEvent) {}
}
