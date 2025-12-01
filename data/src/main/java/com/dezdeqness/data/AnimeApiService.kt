package com.dezdeqness.data

import com.dezdeqness.data.core.NeedAuthorization
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.model.AnimeChronologyResponseRemote
import com.dezdeqness.data.model.AnimeDetailsRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface AnimeApiService {

    @GET("animes/")
    fun getListAnime(
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @Query(value = "order") order: String = "ranked",
        @Query(value = "censored") isAdultContentEnabled: Boolean,
        @QueryMap options: Map<String, String> = mapOf(),
    ): Call<List<AnimeBriefRemote>>

    @GET("animes/")
    fun getListAnimeWithSearchQuery(
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @Query(value = "search") search: String,
        @Query(value = "order") order: String = "ranked",
        @Query(value = "censored") isAdultContentEnabled: Boolean,
        @QueryMap options: Map<String, String> = mapOf(),
    ): Call<List<AnimeBriefRemote>>

    @GET("animes/{id}")
    fun getDetailsAnimeMainInfo(
        @Path(value = "id") id: Long,
    ): Call<AnimeDetailsRemote>

    @NeedAuthorization
    @GET("animes/{id}")
    fun getDetailsAnimeMainInfoWithAuth(
        @Path(value = "id") id: Long,
    ): Call<AnimeDetailsRemote>

    @GET("animes/{id}/similar")
    fun getDetailsAnimeSimilar(
        @Path(value = "id") id: Long,
    ): Call<List<AnimeBriefRemote>>

    @GET("animes/{id}/franchise")
    fun getDetailsAnimeChronology(
        @Path(value = "id") id: Long,
    ): Call<AnimeChronologyResponseRemote>

}
