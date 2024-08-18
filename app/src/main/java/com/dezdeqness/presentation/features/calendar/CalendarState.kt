package com.dezdeqness.presentation.features.calendar

data class CalendarState(
    val list: List<CalendarListUiModel> = listOf(),
    val isPullDownRefreshing: Boolean = false,
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
    val isErrorStateShowing: Boolean = false,
    val isScrollNeed: Boolean = false,
)
