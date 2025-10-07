package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AnimeCalendarEntity

interface CalendarRepository {

    suspend fun getCalendar(): Result<List<AnimeCalendarEntity>>

}
