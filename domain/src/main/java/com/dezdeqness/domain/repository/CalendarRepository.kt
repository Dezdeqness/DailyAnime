package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AnimeCalendarEntity

interface CalendarRepository {

    fun getCalendar(): Result<List<AnimeCalendarEntity>>

}
