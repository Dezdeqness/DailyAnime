package com.dezdeqness.feature.calendar.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CalendarApiService {

    @GET("calendar/")
    fun getCalendar(@Query("censored") isAdultContentEnabled: Boolean): Call<List<AnimeCalendarRemote>>
}
