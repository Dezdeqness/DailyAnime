package com.dezdeqness.presentation.features.history

import com.dezdeqness.presentation.features.history.models.HistoryModel

data class HistoryState(
    val list: List<HistoryModel> = listOf(),
    val isEmptyStateShowing: Boolean = false,
    private val isInitialLoadingIndicatorShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val hasNextPage: Boolean = false,
    val isErrorStateShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
) {

    val isLoadingStateShowing get() =
        list.isEmpty() && isEmptyStateShowing.not() && isInitialLoadingIndicatorShowing
}
