package com.dezdeqness.data

import com.dezdeqness.data.model.TopicRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TopicApiService {

    @GET("topics")
    fun getTopics(
        @Query("forum") forumType: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Call<List<TopicRemote>>

}
