package com.dezdeqness.data

import com.dezdeqness.data.model.AnimeDetailsRemote
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.model.RelatedItemRemote
import com.dezdeqness.data.model.RoleRemote
import com.dezdeqness.data.model.ScreenshotRemote
import retrofit2.Call
import retrofit2.http.*

interface AnimeApiService {

    @GET("animes/")
    fun getListAnime(
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @Query(value = "order") order: String = "ranked",
        @QueryMap options: Map<String, String> = mapOf(),
    ): Call<List<AnimeBriefRemote>>

    @GET("animes/")
    fun getListAnimeWithSearchQuery(
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @Query(value = "search") search: String,
        @Query(value = "order") order: String = "ranked",
        @QueryMap options: Map<String, String> = mapOf(),
    ): Call<List<AnimeBriefRemote>>

    @GET("animes/{id}")
    fun getDetailsAnimeMainInfo(
        @Path(value = "id") id: Long,
    ): Call<AnimeDetailsRemote>

    @GET("animes/{id}")
    fun getDetailsAnimeMainInfo(
        @Path(value = "id") id: Long,
        @Header(value = "User-Agent") agent: String = "Shikimori Android APP",
        @Header(value = "Authorization") token: String,
    ): Call<AnimeDetailsRemote>

    @GET("animes/{id}/screenshots")
    fun getDetailsAnimeScreenshots(
        @Path(value = "id") id: Long,
    ): Call<List<ScreenshotRemote>>

    @GET("animes/{id}/related")
    fun getDetailsAnimeRelated(
        @Path(value = "id") id: Long,
    ): Call<List<RelatedItemRemote>>

    @GET("animes/{id}/roles")
    fun getDetailsAnimeRoles(
        @Path(value = "id") id: Long,
    ): Call<List<RoleRemote>>

    @GET("animes/{id}/similar")
    fun getDetailsAnimeSimilar(
        @Path(value = "id") id: Long,
    ): Call<List<AnimeBriefRemote>>
}
