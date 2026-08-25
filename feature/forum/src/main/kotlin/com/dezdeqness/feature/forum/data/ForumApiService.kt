package com.dezdeqness.feature.forum.data

import com.dezdeqness.data.model.ForumRemote
import retrofit2.Call
import retrofit2.http.GET

internal interface ForumApiService {

    @GET("forums")
    fun getForums(): Call<List<ForumRemote>>
}
