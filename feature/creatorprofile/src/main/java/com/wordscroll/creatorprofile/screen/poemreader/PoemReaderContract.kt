package com.wordscroll.creatorprofile.screen.poemreader

import com.wordscroll.data.model.PoemModel

data class ViewState(
    val poems: List<PoemModel>? = null,
    val bookmarkedIds: Set<String> = emptySet(),
)

sealed class PoemReaderEvent {
    data class ToggleBookmark(val poem: PoemModel) : PoemReaderEvent()
}
