package com.dezdeqness.data

import com.dezdeqness.data.model.favorite.FavoritesResponse
import com.dezdeqness.data.model.requet.FavouriteReorderRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavouriteApiService {
    @GET("/api/users/{id}/favourites")
    suspend fun getUserFavourites(
        @Path("id") userId: Long
    ): Response<FavoritesResponse>

    @POST("/api/favorites/{linked_type}/{linked_id}/{kind}")
    suspend fun addFavoriteWithKind(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Long,
        @Path("kind") kind: String
    ): Response<ResponseBody>

    @POST("/api/favorites/{linked_type}/{linked_id}")
    suspend fun addFavorite(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Long
    ): Response<ResponseBody>

    @DELETE("/api/favorites/{linked_type}/{linked_id}")
    suspend fun deleteFavorite(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Long
    ): Response<ResponseBody>

    @POST("/api/favorites/{id}/reorder")
    suspend fun reorderFavorite(
        @Path("id") favoriteId: Long,
        @Body body: FavouriteReorderRequest,
    ): Response<ResponseBody>
}
