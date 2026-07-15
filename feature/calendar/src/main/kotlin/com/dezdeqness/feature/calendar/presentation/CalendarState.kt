package com.dezdeqness.feature.calendar.presentation

import androidx.compose.runtime.Immutable
import com.dezdeqness.feature.calendar.presentation.models.CalendarListUiModel

@Immutable
data class CalendarState(
    val list: List<CalendarListUiModel> = listOf(),
    val status: CalendarStatus = CalendarStatus.Initial,
)

enum class CalendarStatus {
    Initial,
    Error,
    Empty,
    Loaded,
}
