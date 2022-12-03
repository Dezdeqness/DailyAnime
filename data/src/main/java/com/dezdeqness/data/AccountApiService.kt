package com.dezdeqness.data

import com.dezdeqness.data.model.AccountRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AccountApiService {

    @GET("users/whoami")
    fun getProfile(
        @Header(value = "Authorization") token: String,
        @Header(value = "User-Agent") agent: String = "Shikimori Android APP"
    ): Call<AccountRemote>

    @GET("users/{id}")
    fun getProfileDetails(
        @Path(value = "id") id: Long,
        @Header(value = "Authorization") token: String,
        @Header(value = "User-Agent") agent: String = "Shikimori Android APP"
    ): Call<AccountRemote>

}
