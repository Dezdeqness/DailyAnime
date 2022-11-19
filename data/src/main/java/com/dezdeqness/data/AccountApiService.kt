package com.dezdeqness.data

import com.dezdeqness.data.model.AccountRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface AccountApiService {

    @GET("users/whoami")
    fun getProfile(
        @Header(value = "Authorization") token: String,
        @Header(value = "User-Agent") agent: String = "Shikimori Android APP"
    ): Call<AccountRemote>

}
