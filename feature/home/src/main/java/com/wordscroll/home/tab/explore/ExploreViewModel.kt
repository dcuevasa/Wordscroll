package com.wordscroll.home.tab.explore

import androidx.lifecycle.viewModelScope
import com.wordscroll.core.base.BaseViewModel
import com.wordscroll.domain.poet.GetPoetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getPoetsUseCase: GetPoetsUseCase
) : BaseViewModel<ViewState, ExploreEvent>() {

    init {
        viewModelScope.launch {
            getPoetsUseCase().collect { updateState(ViewState(poets = it)) }
        }
    }

    override fun onTriggerEvent(event: ExploreEvent) {
    }
}
