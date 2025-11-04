package com.dezdeqness.data

import com.dezdeqness.data.model.favorite.FavouritesResponse
import com.dezdeqness.data.model.requet.FavouriteReorderRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavouriteApiService {
    @GET("/api/users/{id}/favourites")
    fun getUserFavourites(
        @Path("id") userId: Long
    ): Call<FavouritesResponse>

    @POST("/api/favorites/{linked_type}/{linked_id}/{kind}")
    fun addFavoriteWithKind(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Long,
        @Path("kind") kind: String
    ): Call<ResponseBody>

    @POST("/api/favorites/{linked_type}/{linked_id}")
    fun addFavorite(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Long
    ): Call<ResponseBody>

    @DELETE("/api/favorites/{linked_type}/{linked_id}")
    fun deleteFavorite(
        @Path("linked_type") linkedType: String,
        @Path("linked_id") linkedId: Long
    ): Call<ResponseBody>

    @POST("/api/favorites/{id}/reorder")
    fun reorderFavorite(
        @Path("id") favoriteId: Long,
        @Body body: FavouriteReorderRequest,
    ): Call<ResponseBody>
}
