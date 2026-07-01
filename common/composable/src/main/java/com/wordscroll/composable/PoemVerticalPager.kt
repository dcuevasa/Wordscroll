package com.wordscroll.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordscroll.core.extension.Space
import com.wordscroll.core.utils.IntentUtils.share
import com.wordscroll.data.model.PoemModel
import com.wordscroll.theme.R

/** Max lines shown per horizontal page of a poem before it spills to the next page. */
private const val LINES_PER_PAGE = 8

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PoemVerticalPager(
    modifier: Modifier = Modifier,
    poems: List<PoemModel>,
    initialPage: Int = 0,
    bookmarkedIds: Set<String> = emptySet(),
    onClickAuthor: (author: String) -> Unit,
    onToggleBookmark: (poem: PoemModel) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = initialPage)

    // Vertical swipe = move between poems. One poem per page.
    VerticalPager(
        pageCount = poems.size,
        state = pagerState,
        beyondBoundsPageCount = 1,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val poem = poems[page]
        PoemPage(
            poem = poem,
            isBookmarked = bookmarkedIds.contains(poem.id),
            onClickAuthor = onClickAuthor,
            onToggleBookmark = onToggleBookmark
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PoemPage(
    poem: PoemModel,
    isBookmarked: Boolean,
    onClickAuthor: (author: String) -> Unit,
    onToggleBookmark: (poem: PoemModel) -> Unit,
) {
    val context = LocalContext.current
    val pages = remember(poem.id) { paginateLines(poem.lines) }
    val innerPager = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // The poem sits in the middle ~60% of the screen, with breathing room
        // above and below and comfortable horizontal margins.
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(horizontal = 30.dp),
        ) {
            Text(
                text = poem.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontStyle = FontStyle.Italic,
                    lineHeight = 26.sp
                ),
                maxLines = 2
            )
            16.dp.Space()

            // Horizontal swipe = flip through the pages of a long poem.
            HorizontalPager(
                pageCount = pages.size,
                state = innerPager,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { p ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    pages[p].forEach { line ->
                        if (line.isBlank()) {
                            10.dp.Space()
                        } else {
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 26.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }
                }
            }

            if (pages.size > 1) {
                12.dp.Space()
                PageDots(count = pages.size, selected = innerPager.currentPage)
            }
        }

        // Author — tap to open the poet's profile.
        Text(
            text = "— ${poem.author}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 30.dp, end = 88.dp, bottom = 30.dp)
                .clickable { onClickAuthor(poem.author) }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(onClick = { onToggleBookmark(poem) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = null,
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    modifier = Modifier.size(26.dp)
                )
            }
            IconButton(onClick = {
                context.share(
                    text = buildString {
                        append(poem.title)
                        append("\n")
                        append(poem.author)
                        append("\n\n")
                        append(poem.lines.joinToString("\n"))
                    }
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun PageDots(count: Int, selected: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == selected) 7.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

/** Split a poem into pages of at most [LINES_PER_PAGE] lines, trimming blank edges. */
private fun paginateLines(lines: List<String>): List<List<String>> {
    if (lines.isEmpty()) return listOf(listOf(""))
    return lines.chunked(LINES_PER_PAGE)
        .map { chunk -> chunk.dropWhile { it.isBlank() }.dropLastWhile { it.isBlank() } }
        .filter { it.isNotEmpty() }
        .ifEmpty { listOf(lines) }
}
