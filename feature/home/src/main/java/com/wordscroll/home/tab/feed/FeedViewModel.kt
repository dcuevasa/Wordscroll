package com.wordscroll.home.tab.feed

import androidx.lifecycle.viewModelScope
import com.wordscroll.core.base.BaseViewModel
import com.wordscroll.domain.bookmark.GetBookmarksUseCase
import com.wordscroll.domain.bookmark.ToggleBookmarkUseCase
import com.wordscroll.domain.poem.GetPoemFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPoemFeedUseCase: GetPoemFeedUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<ViewState, FeedEvent>() {

    init {
        viewModelScope.launch {
            getPoemFeedUseCase().combine(getBookmarksUseCase()) { poems, bookmarks ->
                ViewState(poems = poems, bookmarkedIds = bookmarks.map { it.id }.toSet())
            }.collect { updateState(it) }
        }
    }

    override fun onTriggerEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.ToggleBookmark -> viewModelScope.launch {
                toggleBookmarkUseCase(event.poem)
            }
        }
    }
}
