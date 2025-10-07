package com.dezdeqness.data

import com.dezdeqness.data.model.AnimeCalendarRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApiService {

    @GET("calendar/")
    fun getCalendar(@Query("censored") isAdultContentEnabled: Boolean): Call<List<AnimeCalendarRemote>>

}
