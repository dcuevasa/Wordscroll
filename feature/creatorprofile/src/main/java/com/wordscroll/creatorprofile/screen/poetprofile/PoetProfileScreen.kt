package com.wordscroll.creatorprofile.screen.poetprofile

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wordscroll.composable.TopBar
import com.wordscroll.core.DestinationRoute.POEM_READER_ROUTE
import com.wordscroll.core.extension.Space
import com.wordscroll.creatorprofile.screen.poetprofile.component.PoemListItem
import com.wordscroll.theme.R
import com.wordscroll.theme.SubTextColor
import kotlin.math.absoluteValue

private val AvatarPalette = listOf(
    Color(0xFFB0623B), Color(0xFF5B7B7A), Color(0xFF8C6A4F),
    Color(0xFF6E5773), Color(0xFF4F6B8C), Color(0xFF7A6A53),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoetProfileScreen(
    onClickNavIcon: () -> Unit,
    navController: NavController,
    viewModel: PoetProfileViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val poetName = viewState?.poetName
    val poems = viewState?.poems
    val bookmarkedIds = viewState?.bookmarkedIds ?: emptySet()

    Scaffold(
        topBar = {
            TopBar(onClickNavIcon = onClickNavIcon, title = poetName.orEmpty())
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            PoetHeader(name = poetName, poemCount = poems?.size)
            16.dp.Space()
            if (poems != null) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(poems, key = { _, poem -> poem.id }) { index, poem ->
                        PoemListItem(
                            poem = poem,
                            isBookmarked = bookmarkedIds.contains(poem.id),
                            onClick = {
                                navController.navigate(
                                    "$POEM_READER_ROUTE/${Uri.encode(poetName.orEmpty())}/$index"
                                )
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun PoetHeader(name: String?, poemCount: Int?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val avatarColor = AvatarPalette[(name.orEmpty().hashCode().absoluteValue) % AvatarPalette.size]
        val initials = name.orEmpty().trim().split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")

        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials, color = Color.White, style = MaterialTheme.typography.headlineMedium)
        }
        12.dp.Space()
        Text(
            text = name.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        if (poemCount != null) {
            4.dp.Space()
            Text(
                text = stringResource(id = R.string.poem_count, poemCount),
                style = MaterialTheme.typography.bodySmall,
                color = SubTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
