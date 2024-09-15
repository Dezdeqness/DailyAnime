package com.dezdeqness.data.datasource

import com.dezdeqness.data.CalendarApiService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.CalendarMapper
import dagger.Lazy
import javax.inject.Inject

class CalendarRemoteDataSourceImpl @Inject constructor(
    private val calendarApiService: Lazy<CalendarApiService>,
    private val calendarMapper: CalendarMapper,
) : CalendarRemoteDataSource, BaseDataSource() {

    override fun getCalendar() = tryWithCatch {
        val response = calendarApiService.get().getCalendar().execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                responseBody.mapNotNull(calendarMapper::fromResponse)
            )
        } else {
            throw response.createApiException()
        }
    }
}
