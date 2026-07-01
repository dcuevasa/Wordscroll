package com.wordscroll.home.tab.feed

import com.wordscroll.data.model.PoemModel

data class ViewState(
    val poems: List<PoemModel> = emptyList(),
    val bookmarkedIds: Set<String> = emptySet(),
)

sealed class FeedEvent {
    data class ToggleBookmark(val poem: PoemModel) : FeedEvent()
}
