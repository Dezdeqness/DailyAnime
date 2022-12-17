package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.presentation.models.AdapterItem

data class CalendarState(
    val items: List<AdapterItem> = listOf(),
    val isPullDownRefreshing: Boolean = false,
    val scrollToTop: Boolean = false,
)
