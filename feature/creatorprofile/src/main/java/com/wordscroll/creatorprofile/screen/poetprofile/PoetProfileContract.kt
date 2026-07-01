package com.wordscroll.creatorprofile.screen.poetprofile

import com.wordscroll.data.model.PoemModel

data class ViewState(
    val poetName: String? = null,
    val poems: List<PoemModel>? = null,
    val bookmarkedIds: Set<String> = emptySet(),
)

sealed class PoetProfileEvent {
    data class ToggleBookmark(val poem: PoemModel) : PoetProfileEvent()
}
