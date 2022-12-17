package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.AnimeCalendarEntity

interface CalendarRemoteDataSource {

    fun getCalendar(): Result<List<AnimeCalendarEntity>>

}
