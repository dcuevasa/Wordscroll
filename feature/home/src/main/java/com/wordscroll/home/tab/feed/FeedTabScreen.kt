package com.wordscroll.home.tab.feed

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wordscroll.composable.PoemVerticalPager
import com.wordscroll.core.DestinationRoute.POET_PROFILE_ROUTE
import com.wordscroll.theme.R
import com.wordscroll.theme.SubTextColor

@Composable
fun FeedTabScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val poems = viewState?.poems

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            viewState == null -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            poems.isNullOrEmpty() -> {
                Text(
                    text = stringResource(id = R.string.poems_load_error),
                    color = SubTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
            else -> {
                PoemVerticalPager(
                    poems = poems,
                    bookmarkedIds = viewState?.bookmarkedIds ?: emptySet(),
                    onClickAuthor = { author ->
                        navController.navigate("$POET_PROFILE_ROUTE/${Uri.encode(author)}")
                    },
                    onToggleBookmark = { poem ->
                        viewModel.onTriggerEvent(FeedEvent.ToggleBookmark(poem))
                    }
                )
            }
        }
    }
}
