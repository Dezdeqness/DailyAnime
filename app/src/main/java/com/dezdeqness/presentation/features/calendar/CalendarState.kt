package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.presentation.models.AdapterItem

data class CalendarState(
    val items: List<AdapterItem> = listOf(),
    val isPullDownRefreshing: Boolean = false,
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isErrorStateShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
)
