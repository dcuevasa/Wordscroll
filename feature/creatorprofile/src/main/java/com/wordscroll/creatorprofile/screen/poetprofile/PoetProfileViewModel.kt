package com.wordscroll.creatorprofile.screen.poetprofile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wordscroll.core.DestinationRoute.PassedKey.POET_NAME
import com.wordscroll.core.base.BaseViewModel
import com.wordscroll.domain.bookmark.GetBookmarksUseCase
import com.wordscroll.domain.bookmark.ToggleBookmarkUseCase
import com.wordscroll.domain.poem.GetPoemsByPoetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PoetProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPoemsByPoetUseCase: GetPoemsByPoetUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<ViewState, PoetProfileEvent>() {

    val poetName: String? = savedStateHandle.get<String>(POET_NAME)?.let { Uri.decode(it) }

    init {
        poetName?.let { name ->
            viewModelScope.launch {
                getPoemsByPoetUseCase(name).combine(getBookmarksUseCase()) { poems, bookmarks ->
                    ViewState(
                        poetName = name,
                        poems = poems,
                        bookmarkedIds = bookmarks.map { it.id }.toSet()
                    )
                }.collect { updateState(it) }
            }
        }
    }

    override fun onTriggerEvent(event: PoetProfileEvent) {
        when (event) {
            is PoetProfileEvent.ToggleBookmark -> viewModelScope.launch {
                toggleBookmarkUseCase(event.poem)
            }
        }
    }
}
