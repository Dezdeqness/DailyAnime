package com.dezdeqness.data

import com.dezdeqness.data.model.AnimeCalendarRemote
import retrofit2.Call
import retrofit2.http.GET

interface CalendarApiService {

    @GET("calendar/")
    fun getCalendar(): Call<List<AnimeCalendarRemote>>

}
