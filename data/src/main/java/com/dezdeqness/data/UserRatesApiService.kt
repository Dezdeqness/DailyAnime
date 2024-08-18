package com.dezdeqness.data

import com.dezdeqness.data.core.NeedAuthorization
import com.dezdeqness.data.model.UserRateRemote
import com.dezdeqness.data.model.requet.PostUserRateRequestBody
import com.dezdeqness.data.model.requet.UpdateUserRateRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserRatesApiService {

    @GET("users/{id}/anime_rates")
    fun getUserRates(
        @Path(value = "id") id: Long,
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @Query(value = "status") status: String,
    ): Call<List<UserRateRemote>>

    @NeedAuthorization
    @PATCH("v2/user_rates/{id}")
    fun updateUserRate(
        @Path(value = "id") id: Long,
        @Body body: UpdateUserRateRequestBody,
    ): Call<UserRateRemote>

    @NeedAuthorization
    @POST("v2/user_rates")
    fun createUserRate(
        @Body body: PostUserRateRequestBody,
    ): Call<UserRateRemote>

    @NeedAuthorization
    @DELETE("v2/user_rates/{id}")
    fun deleteUserRate(@Path(value = "id") id: Long): Call<ResponseBody>

}
