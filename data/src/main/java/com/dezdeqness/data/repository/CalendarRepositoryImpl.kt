package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.CalendarRemoteDataSource
import com.dezdeqness.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
) : CalendarRepository {

    override fun getCalendar() = calendarRemoteDataSource.getCalendar()

}
