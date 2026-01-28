package com.dezdeqness.feature.personallist.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.feature.personallist.model.UserRateUiModel
import com.dezdeqness.feature.personallist.tab.PersonalListAction

private const val PAGINATION_LOAD_FACTOR = 0.75

@Composable
fun PersonalList(
    modifier: Modifier = Modifier,
    list: List<UserRateUiModel>,
    hasNextPage: Boolean,
    isPageLoading: Boolean,
    onActionReceive: (PersonalListAction) -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    val shouldStartPaginate = remember(hasNextPage) {
        derivedStateOf {
            hasNextPage && (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -1) >= (listState.layoutInfo.totalItemsCount * PAGINATION_LOAD_FACTOR)
        }
    }

    LaunchedEffect(isPageLoading, shouldStartPaginate.value) {
        if (shouldStartPaginate.value && isPageLoading.not()) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(
            count = list.size,
            key = { index -> list[index].id + list[index].name.hashCode() },
        ) { index ->
            PersonalListAnimeItem(
                userRateUiModel = list[index],
                modifier = Modifier
                    .animateItem()
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    ),
                onActionReceive = onActionReceive,
            )
        }

        if (hasNextPage) {
            item {
                ShimmerPersonalItem(
                    modifier = Modifier.padding(
                        vertical = 4.dp,
                        horizontal = 16.dp,
                    )
                )
            }
        }
    }
}
