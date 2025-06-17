package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.features.animelist.AnimeUiModel
import kotlinx.coroutines.flow.distinctUntilChanged

private const val PAGINATION_LOAD_FACTOR = 0.75

@Composable
fun AnimeSearchGrid(
    modifier: Modifier = Modifier,
    list: List<AnimeUiModel>,
    hasNextPage: Boolean,
    isPageLoading: Boolean,
    isScrollNeed: Boolean,
    onLoadMore: () -> Unit,
    onNeedScroll: (LazyGridState) -> Unit,
    onActionReceive: (Action) -> Unit,
    onScrollInProgress: (Boolean) -> Unit,
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { onScrollInProgress(it) }
    }

    val shouldStartPaginate = remember {
        derivedStateOf {
            hasNextPage && (gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -1) >= (gridState.layoutInfo.totalItemsCount * PAGINATION_LOAD_FACTOR)
        }
    }

    LaunchedEffect(isScrollNeed) {
        if (isScrollNeed) {
            onNeedScroll(gridState)
        }
    }

    LaunchedEffect(isPageLoading, shouldStartPaginate.value) {
        if (shouldStartPaginate.value && isPageLoading.not()) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = list.size,
            key = { index ->
                val item = list[index]
                item.id + item.title.hashCode()
            }
        ) { index ->
            val item = list[index]

            val column = index % 2

            val left = 8.dp - column * 8.dp / 2
            val right = (column + 1) * 8.dp / 2

            AnimeItem(
                item = item,
                modifier = Modifier
                    .animateItem()
                    .padding(
                        start = left, end = right, top = 8.dp
                    ),
                onClick = { id ->
                    onActionReceive(Action.AnimeClick(animeId = id, title = item.title))
                }
            )
        }

        if (hasNextPage) {
            item {
                ShimmerSearchItemLoading(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                        .padding(vertical = 8.dp),
                )
            }
            item {
                ShimmerSearchItemLoading(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                        .padding(vertical = 8.dp),
                )
            }
        }

    }
}
