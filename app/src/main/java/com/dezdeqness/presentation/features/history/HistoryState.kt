package com.dezdeqness.presentation.features.history

import com.dezdeqness.presentation.models.AdapterItem

data class HistoryState(
    val list: List<AdapterItem> = listOf(),
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isPullDownRefreshing: Boolean = false,
    val hasNextPage: Boolean = false,
)
