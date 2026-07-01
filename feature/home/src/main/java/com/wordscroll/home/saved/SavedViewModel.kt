package com.wordscroll.home.saved

import androidx.lifecycle.viewModelScope
import com.wordscroll.core.base.BaseViewModel
import com.wordscroll.domain.bookmark.GetBookmarksUseCase
import com.wordscroll.domain.bookmark.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<ViewState, SavedEvent>() {

    init {
        viewModelScope.launch {
            getBookmarksUseCase().collect { updateState(ViewState(poems = it)) }
        }
    }

    override fun onTriggerEvent(event: SavedEvent) {
        when (event) {
            is SavedEvent.ToggleBookmark -> viewModelScope.launch {
                toggleBookmarkUseCase(event.poem)
            }
        }
    }
}
