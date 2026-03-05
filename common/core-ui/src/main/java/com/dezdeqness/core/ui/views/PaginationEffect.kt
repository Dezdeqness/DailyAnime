package com.dezdeqness.core.ui.views

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

private const val DEFAULT_PAGINATION_LOAD_FACTOR = 0.75f

@Composable
fun PaginationEffect(
    listState: LazyListState,
    hasNextPage: Boolean,
    isPageLoading: Boolean,
    loadFactor: Float = DEFAULT_PAGINATION_LOAD_FACTOR,
    onLoadMore: () -> Unit,
) {
    val shouldStartPaginate = remember(hasNextPage) {
        derivedStateOf {
            hasNextPage && (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -1) >= (listState.layoutInfo.totalItemsCount * loadFactor)
        }
    }

    LaunchedEffect(isPageLoading, shouldStartPaginate.value) {
        if (shouldStartPaginate.value && isPageLoading.not()) {
            onLoadMore()
        }
    }
}
