package com.wordscroll.home.saved

import com.wordscroll.data.model.PoemModel

data class ViewState(
    val poems: List<PoemModel>? = null,
)

sealed class SavedEvent {
    data class ToggleBookmark(val poem: PoemModel) : SavedEvent()
}
