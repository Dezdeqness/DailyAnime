package com.dezdeqness.feature.calendar.presentation.models

data class CalendarListUiModel(
    val header: String,
    val items: List<CalendarUiModel>,
)

data class CalendarUiModel(
    val id: Long,
    val name: String,
    val ongoingEpisode: Int,
    val type: String,
    val score: String,
    val time: String,
    val logoUrl: String,
)
