package com.dezdeqness.feature.calendar.data

import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import dagger.Lazy
import javax.inject.Inject

internal class CalendarRemoteDataSourceImpl @Inject constructor(
    private val calendarApiService: Lazy<CalendarApiService>,
    private val calendarMapper: CalendarMapper,
) : CalendarRemoteDataSource, BaseDataSource() {

    override fun getCalendar(isAdultContentEnabled: Boolean) = tryWithCatch {
        val response = calendarApiService.get().getCalendar(isAdultContentEnabled = isAdultContentEnabled).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                responseBody.mapNotNull(calendarMapper::fromResponse),
            )
        } else {
            throw response.createApiException()
        }
    }
}
