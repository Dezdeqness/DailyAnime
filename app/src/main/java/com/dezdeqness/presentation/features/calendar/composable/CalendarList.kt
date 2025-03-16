package com.dezdeqness.presentation.features.calendar.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.features.calendar.CalendarListUiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarList(
    modifier: Modifier = Modifier,
    list: List<CalendarListUiModel>,
    isScrollNeed: Boolean,
    onNeedScroll: (LazyListState) -> Unit,
    onActionReceive: (Action) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(isScrollNeed) {
        if (isScrollNeed) {
            onNeedScroll(listState)
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        list.forEach { section ->
            stickyHeader {
                CalendarDateHeader(
                    title = section.header,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            items(
                count = section.items.size,
                key = { index -> section.items[index].id + section.items[index].name.hashCode() },
            ) { index ->
                CalendarItem(
                    item = section.items[index],
                    modifier = Modifier
                        .animateItem()
                        .padding(
                            vertical = 4.dp,
                            horizontal = 16.dp,
                        ),
                    onClick = { id, name ->
                        onActionReceive(Action.AnimeClick(id,  title = name))
                    },
                )
            }
        }
    }
}
