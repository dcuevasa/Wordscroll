package com.wordscroll.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordscroll.core.extension.Space
import com.wordscroll.core.settings.ImageCorner
import com.wordscroll.core.utils.IntentUtils.share
import com.wordscroll.data.model.PoemModel
import com.wordscroll.theme.LocalAppTheme
import com.wordscroll.theme.R
import com.wordscroll.theme.rememberBackgroundPainter
import com.wordscroll.theme.rememberCompanionImagePainter
import com.wordscroll.theme.textDecoration
import com.wordscroll.theme.toFontFamily

private val CompanionImageSize = 56.dp

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
    val theme = LocalAppTheme.current
    val backgroundPainter = theme.rememberBackgroundPainter()
    val companionPainter = theme.rememberCompanionImagePainter()

    // The poem's own text styles — deliberately independent from
    // MaterialTheme.typography (which only scales app-chrome/menu text) so the
    // poem font size has its own, dedicated control.
    val titleStyle = remember(theme) {
        TextStyle(
            fontFamily = theme.font.toFontFamily(),
            fontStyle = FontStyle.Italic,
            color = Color(theme.textColor),
            fontSize = 22.sp * theme.poemFontScale,
            lineHeight = 28.sp * theme.poemFontScale
        )
    }
    val lineStyle = remember(theme) {
        TextStyle(
            fontFamily = theme.font.toFontFamily(),
            fontStyle = if (theme.italic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = theme.textDecoration(),
            color = Color(theme.textColor),
            fontSize = 17.sp * theme.poemFontScale,
            lineHeight = 26.sp * theme.poemFontScale
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (backgroundPainter != null) {
            Image(
                painter = backgroundPainter,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
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
                style = titleStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
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
                            Text(text = line, style = lineStyle)
                        }
                    }
                }
            }

            if (pages.size > 1) {
                12.dp.Space()
                PageDots(count = pages.size, selected = innerPager.currentPage)
            }
        }

        if (companionPainter != null) {
            Image(
                painter = companionPainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(theme.companionImageCorner.toAlignment())
                    .padding(theme.companionImageCorner.toSafePadding())
                    .size(CompanionImageSize)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(1.5.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)),
                        CircleShape
                    )
            )
        }

        // Author — tap to open the poet's profile.
        Text(
            text = "— ${poem.author}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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

private fun ImageCorner.toAlignment(): Alignment = when (this) {
    ImageCorner.NONE, ImageCorner.TOP_START -> Alignment.TopStart
    ImageCorner.TOP_END -> Alignment.TopEnd
    ImageCorner.BOTTOM_START -> Alignment.BottomStart
    ImageCorner.BOTTOM_END -> Alignment.BottomEnd
}

/**
 * Padding that keeps the companion image clear of everything else drawn on
 * the page: the back arrow some screens overlay top-start, the author label
 * bottom-start, and the bookmark/share icon column bottom-end.
 */
private fun ImageCorner.toSafePadding(): PaddingValues = when (this) {
    ImageCorner.NONE, ImageCorner.TOP_START -> PaddingValues(start = 20.dp, top = 90.dp)
    ImageCorner.TOP_END -> PaddingValues(end = 20.dp, top = 24.dp)
    ImageCorner.BOTTOM_START -> PaddingValues(start = 20.dp, bottom = 76.dp)
    ImageCorner.BOTTOM_END -> PaddingValues(end = 16.dp, bottom = 112.dp)
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
