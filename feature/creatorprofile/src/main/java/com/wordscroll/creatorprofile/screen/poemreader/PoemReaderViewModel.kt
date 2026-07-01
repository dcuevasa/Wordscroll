package com.wordscroll.creatorprofile.screen.poemreader

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wordscroll.core.DestinationRoute.PassedKey.POEM_INDEX
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
class PoemReaderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPoemsByPoetUseCase: GetPoemsByPoetUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<ViewState, PoemReaderEvent>() {

    val poetName: String? = savedStateHandle.get<String>(POET_NAME)?.let { Uri.decode(it) }
    val poemIndex: Int? = savedStateHandle[POEM_INDEX]

    init {
        poetName?.let { name ->
            viewModelScope.launch {
                getPoemsByPoetUseCase(name).combine(getBookmarksUseCase()) { poems, bookmarks ->
                    ViewState(poems = poems, bookmarkedIds = bookmarks.map { it.id }.toSet())
                }.collect { updateState(it) }
            }
        }
    }

    override fun onTriggerEvent(event: PoemReaderEvent) {
        when (event) {
            is PoemReaderEvent.ToggleBookmark -> viewModelScope.launch {
                toggleBookmarkUseCase(event.poem)
            }
        }
    }
}
