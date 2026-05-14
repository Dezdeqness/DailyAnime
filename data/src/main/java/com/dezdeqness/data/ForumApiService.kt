package com.dezdeqness.data

import com.dezdeqness.data.model.ForumRemote
import retrofit2.Call
import retrofit2.http.GET

interface ForumApiService {

    @GET("forums")
    fun getForums(): Call<List<ForumRemote>>
}
