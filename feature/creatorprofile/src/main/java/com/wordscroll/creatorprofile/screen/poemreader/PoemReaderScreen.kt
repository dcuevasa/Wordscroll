package com.wordscroll.creatorprofile.screen.poemreader

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wordscroll.composable.PoemVerticalPager
import com.wordscroll.theme.R

@Composable
fun PoemReaderScreen(
    onClickNavIcon: () -> Unit,
    viewModel: PoemReaderViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val poems = viewState?.poems

    Box(modifier = Modifier.fillMaxSize()) {
        if (!poems.isNullOrEmpty()) {
            PoemVerticalPager(
                poems = poems,
                initialPage = (viewModel.poemIndex ?: 0).coerceIn(0, poems.lastIndex),
                bookmarkedIds = viewState?.bookmarkedIds ?: emptySet(),
                onClickAuthor = { onClickNavIcon() },
                onToggleBookmark = { poem ->
                    viewModel.onTriggerEvent(PoemReaderEvent.ToggleBookmark(poem))
                }
            )
        } else if (viewState == null) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 20.dp)
                .size(22.dp)
                .clickable { onClickNavIcon() }
        )
    }
}
