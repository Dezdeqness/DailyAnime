package com.dezdeqness.feature.calendar.data

import com.dezdeqness.contract.calendar.model.AnimeCalendarEntity

internal interface CalendarRemoteDataSource {

    fun getCalendar(isAdultContentEnabled: Boolean): Result<List<AnimeCalendarEntity>>
}
