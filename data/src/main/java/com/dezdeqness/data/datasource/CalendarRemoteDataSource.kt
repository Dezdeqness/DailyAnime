package com.dezdeqness.data.datasource

import com.dezdeqness.contract.calendar.model.AnimeCalendarEntity

interface CalendarRemoteDataSource {

    fun getCalendar(isAdultContentEnabled: Boolean): Result<List<AnimeCalendarEntity>>
}
