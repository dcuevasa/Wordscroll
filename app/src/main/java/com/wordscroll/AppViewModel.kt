package com.wordscroll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wordscroll.core.settings.AppLanguage
import com.wordscroll.core.settings.ThemePresets
import com.wordscroll.domain.settings.GetLanguageUseCase
import com.wordscroll.domain.settings.GetSelectedThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    getSelectedThemeUseCase: GetSelectedThemeUseCase,
    getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    val theme = getSelectedThemeUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemePresets.default)

    val language = getLanguageUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppLanguage.SYSTEM)
}
