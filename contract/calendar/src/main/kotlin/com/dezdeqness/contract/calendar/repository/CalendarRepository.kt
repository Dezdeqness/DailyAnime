package com.dezdeqness.contract.calendar.repository

import com.dezdeqness.contract.calendar.model.AnimeCalendarEntity

interface CalendarRepository {

    suspend fun getCalendar(): Result<List<AnimeCalendarEntity>>
}
