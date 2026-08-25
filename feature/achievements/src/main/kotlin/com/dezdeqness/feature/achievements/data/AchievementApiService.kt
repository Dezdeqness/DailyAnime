package com.dezdeqness.feature.achievements.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface AchievementApiService {
    @GET("achievements/")
    fun getAchievementList(@Query(value = "user_id") userId: Long): Call<List<AchievementRemote>>
}
