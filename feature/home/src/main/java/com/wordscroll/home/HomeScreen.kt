package com.wordscroll.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wordscroll.home.tab.explore.ExploreTabScreen
import com.wordscroll.home.tab.feed.FeedTabScreen
import com.wordscroll.theme.R
import com.wordscroll.theme.WordscrollTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val tabItems = arrayListOf(R.string.explore_tab, R.string.feed_tab)
    val pagerState = rememberPagerState(initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    WordscrollTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {

            HorizontalPager(
                pageCount = tabItems.size,
                state = pagerState,
            ) {
                when (it) {
                    0 -> ExploreTabScreen(navController)
                    1 -> FeedTabScreen(navController)
                }
            }

            val edge = LocalConfiguration.current.screenWidthDp.dp.div(2).minus(100.dp)
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                divider = {},
                modifier = Modifier.padding(top = 8.dp),
                indicator = { tabPositions ->
                    val modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .padding(horizontal = 38.dp)

                    TabRowDefaults.Indicator(
                        modifier, color = MaterialTheme.colorScheme.onBackground
                    )
                },
                edgePadding = edge
            ) {

                tabItems.forEachIndexed { index, item ->
                    val isSelected = pagerState.currentPage == index
                    Tab(
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            val textStyle =
                                if (isSelected) MaterialTheme.typography.titleMedium.merge(
                                    TextStyle(color = MaterialTheme.colorScheme.onBackground)
                                )
                                else TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            Text(text = stringResource(id = item), style = textStyle)
                        }
                    )
                }
            }
        }
    }
}
