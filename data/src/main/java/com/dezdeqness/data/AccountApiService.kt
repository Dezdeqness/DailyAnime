package com.dezdeqness.data

import com.dezdeqness.data.core.NeedAuthorization
import com.dezdeqness.data.model.AccountRemote
import com.dezdeqness.data.model.HistoryRemote
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountApiService {

    @NeedAuthorization
    @GET("users/whoami")
    fun getProfile(): Call<AccountRemote>

    @GET("users/{id}")
    fun getProfileDetails(
        @Path(value = "id") id: Long,
    ): Call<AccountRemote>

    @GET("users/{id}/history")
    fun getUserHistory(
        @Path(value = "id") id: Long,
        @Query(value = "limit") limit: Int,
        @Query(value = "page") page: Int,
        @Query(value = "target_type") type: String = "Anime",
    ): Call<List<HistoryRemote>>

    @NeedAuthorization
    @POST("users/sign_out")
    fun logout(): Call<ResponseBody>

}
