package com.dezdeqness.data

import com.dezdeqness.data.model.AnimeDetailsRemote
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.model.RelatedItemRemote
import com.dezdeqness.data.model.RoleRemote
import com.dezdeqness.data.model.ScreenshotRemote
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
        @QueryMap options: Map<String, String> = mapOf(),
    ): Call<List<AnimeBriefRemote>>

    @GET("animes/{id}")
    fun getDetailsAnimeMainInfo(
        @Path(value = "id") id: Long,
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

}
