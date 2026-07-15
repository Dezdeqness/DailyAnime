package com.dezdeqness.feature.calendar.presentation.preview

import com.dezdeqness.feature.calendar.presentation.CalendarActions
import com.dezdeqness.feature.calendar.presentation.models.CalendarListUiModel
import com.dezdeqness.feature.calendar.presentation.models.CalendarUiModel

object CalendarPreviewData {

    val item = CalendarUiModel(
        id = 1L,
        name = "Восстание Лелуша",
        ongoingEpisode = 12,
        type = "tv",
        score = "8.69",
        time = "12:55",
        logoUrl = "",
    )

    val list = listOf(
        CalendarListUiModel(
            header = "Sunday, July 19",
            items = listOf(
                item,
                CalendarUiModel(
                    id = 2L,
                    name = "Ван-Пис",
                    ongoingEpisode = 1122,
                    type = "tv",
                    score = "8.71",
                    time = "18:00",
                    logoUrl = "",
                ),
            ),
        ),
        CalendarListUiModel(
            header = "Monday, July 20",
            items = listOf(
                CalendarUiModel(
                    id = 3L,
                    name = "Блич",
                    ongoingEpisode = 14,
                    type = "tv",
                    score = "8.2",
                    time = "21:30",
                    logoUrl = "",
                ),
            ),
        ),
    )

    val emptyActions = object : CalendarActions {
        override fun onPullDownRefreshed() = Unit
        override fun onScrolled() = Unit
        override fun onAnimeClicked(animeId: Long, title: String) = Unit
        override fun onQueryChanged(query: String) = Unit
    }
}
