package com.dezdeqness.data

import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.model.AnimeChronologyResponseRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeApiService {
    @GET("animes/{id}/similar")
    fun getDetailsAnimeSimilar(
        @Path(value = "id") id: Long,
    ): Call<List<AnimeBriefRemote>>

    @GET("animes/{id}/franchise")
    fun getDetailsAnimeChronology(
        @Path(value = "id") id: Long,
    ): Call<AnimeChronologyResponseRemote>

}
