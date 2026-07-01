package com.wordscroll.home.saved

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun SavedScreen(
    navController: NavController,
    viewModel: SavedViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val poems = viewState?.poems

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            poems != null && poems.isEmpty() -> {
                Text(
                    text = stringResource(id = R.string.no_saved_poems),
                    color = SubTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp)
                )
            }
            !poems.isNullOrEmpty() -> {
                PoemVerticalPager(
                    poems = poems,
                    bookmarkedIds = poems.map { it.id }.toSet(),
                    onClickAuthor = { author ->
                        navController.navigate("$POET_PROFILE_ROUTE/${Uri.encode(author)}")
                    },
                    onToggleBookmark = { poem ->
                        viewModel.onTriggerEvent(SavedEvent.ToggleBookmark(poem))
                    }
                )
            }
        }
    }
}
