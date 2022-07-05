package com.dezdeqness.data

import com.dezdeqness.data.model.AnimeAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface AnimeApiService {

    @GET("animes/")
    fun getListAnime(
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @QueryMap options: Map<String, String> = mapOf(),
    ): Call<List<AnimeAPI>>

}
