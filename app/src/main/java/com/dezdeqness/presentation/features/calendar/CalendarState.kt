package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.models.AdapterItem

data class CalendarState(
    val items: List<AdapterItem> = listOf(),
    val isPullDownRefreshing: Boolean = false,
    val events: List<Event> = listOf(),
    val isInitialLoadingIndicatorShowing: Boolean = false,
    val isEmptyStateShowing: Boolean = false,
)
