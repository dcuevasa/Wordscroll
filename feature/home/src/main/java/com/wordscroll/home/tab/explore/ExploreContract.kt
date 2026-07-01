package com.wordscroll.home.tab.explore

import com.wordscroll.data.model.PoetModel

data class ViewState(
    val poets: List<PoetModel>? = null,
)

sealed class ExploreEvent
