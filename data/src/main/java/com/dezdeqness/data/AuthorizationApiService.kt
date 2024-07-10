package com.dezdeqness.data

import com.dezdeqness.data.model.TokenRemote
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthorizationApiService {

    @POST("oauth/token")
    fun login(
        @Query(value = "grant_type") type: String = "authorization_code",
        @Query(value = "redirect_uri") uri: String,
        @Query(value = "code") code: String,
        @Query(value = "client_secret") secret: String,
        @Query(value = "client_id") id: String,
    ): Call<TokenRemote>

    @POST("oauth/token")
    fun refresh(
        @Query(value = "grant_type") type: String = "refresh_token",
        @Query(value = "redirect_uri") uri: String,
        @Query(value = "refresh_token") token: String,
        @Query(value = "client_secret") secret: String,
        @Query(value = "client_id") id: String,
    ): Call<TokenRemote>

}
