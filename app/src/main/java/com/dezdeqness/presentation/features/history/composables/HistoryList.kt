package com.dezdeqness.presentation.features.history.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dezdeqness.presentation.features.history.models.HistoryModel

private const val PAGINATION_LOAD_FACTOR = 0.75

@Composable
fun HistoryList(
    modifier: Modifier = Modifier,
    list: List<HistoryModel>,
    hasNextPage: Boolean,
    isPageLoading: Boolean,
    onLoadMore: () -> Unit,
) {

    val state = rememberLazyListState()

    val shouldStartPaginate = remember {
        derivedStateOf {
            hasNextPage && (state.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -1) >= (state.layoutInfo.totalItemsCount * PAGINATION_LOAD_FACTOR)
        }
    }

    LaunchedEffect(isPageLoading, shouldStartPaginate.value) {
        if (shouldStartPaginate.value && isPageLoading.not()) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = state,
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = list.size,
            key = { index ->
                val item = list[index]
                item.id() + item.hashCode()
            }
        ) { index ->
            val item = list[index]

            when (item) {
                is HistoryModel.HistoryUiModel -> {
                    HistoryItem(
                        item = item,
                        modifier = Modifier.animateItem().padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                is HistoryModel.HistoryHeaderUiModel -> {
                    HistoryHeader(
                        header = item.header,
                        modifier = Modifier.animateItem().padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
        }

        if (hasNextPage) {
            item {
                HistoryShimmerItem(modifier = Modifier.padding(horizontal = 16.dp))
            }

        }

    }
}
