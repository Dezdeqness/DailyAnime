package com.dezdeqness.presentation.features.home.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.views.header.Header
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.features.home.model.HomeCalendarUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeCalendarSection(
    modifier: Modifier = Modifier,
    items: List<HomeCalendarUiModel>,
    isCalendarActionVisible: Boolean,
    onActionReceive: (Action) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val itemsSize = items.size
        val pagerSize = itemsSize * 1000
        val middlePage = pagerSize / 2

        val pagerState = rememberPagerState(
            initialPage = middlePage,
            pageCount = { pagerSize },
        )
        val isDraggableState by pagerState.interactionSource.collectIsDraggedAsState()

        val indicatorScrollState = rememberLazyListState()

        LaunchedEffect(isDraggableState) {
            snapshotFlow { isDraggableState }
                .collectLatest {
                    if (!it) {
                        while (true) {
                            delay(5000)
                            pagerState.animateScrollToPage(pagerState.currentPage.inc() % pagerState.pageCount)
                        }
                    }
                }
        }

        Header(
            title = stringResource(R.string.home_calendar_title),
            modifier = Modifier.fillMaxWidth(),
            onClick = if (isCalendarActionVisible) {
                {
                    onActionReceive(Action.CalendarHeaderClicked)
                }
            } else null,
        )
        HorizontalPager(state = pagerState) { index ->
            val currentItem = items[index % itemsSize]
            HomeCalendarItem(
                item = currentItem,
                modifier = Modifier.padding(horizontal = 16.dp),
                onActionReceive = { action ->
                    onActionReceive(action)
                }
            )
        }

        if (itemsSize > 1) {
            Indicator(
                lazyListState = indicatorScrollState,
                itemsSize = itemsSize,
                pagerState = pagerState,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Composable
private fun Indicator(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    itemsSize: Int,
    pagerState: PagerState,
) {
    LazyRow(
        state = lazyListState,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        userScrollEnabled = false,
        modifier = modifier.height(16.dp)
    ) {
        repeat(itemsSize) { iteration ->
            val color =
                if (pagerState.currentPage % itemsSize == iteration) Color.DarkGray else Color.LightGray
            item(key = iteration) {
                val currentPage = pagerState.currentPage % itemsSize
                val firstVisibleIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
                val lastVisibleIndex =
                    lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val size by animateDpAsState(
                    targetValue = if (iteration == currentPage) {
                        10.dp
                    } else if (iteration in firstVisibleIndex + 1..lastVisibleIndex - 1) {
                        10.dp
                    } else {
                        6.dp
                    }
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(color, CircleShape)
                        .size(size)
                )
            }
        }
    }
}
