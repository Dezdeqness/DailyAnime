package com.dezdeqness.data

import com.dezdeqness.data.model.UserRateRemote
import com.dezdeqness.data.model.requet.UserRate
import com.dezdeqness.data.model.requet.UserRateRequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UserRatesApiService {

    @GET("users/{id}/anime_rates")
    fun gerUserRates(
        @Path(value = "id") id: Long,
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @Query(value = "status") status: String,
        @Header(value = "User-Agent") agent: String = "Shikimori Android APP",
        @Header(value = "Authorization") token: String,
    ): Call<List<UserRateRemote>>

    @PATCH("v2/user_rates/{id}")
    fun updateUserRate(
        @Path(value = "id") id: Long,
        @Body body: UserRateRequestBody,
        @Header(value = "User-Agent") agent: String = "Shikimori Android APP",
        @Header(value = "Authorization") token: String,
    ): Call<UserRateRemote>

}
