package com.dezdeqness.data

import com.dezdeqness.data.model.AchievementRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AchievementService {
    @GET("achievements/")
    fun getAchievementList(
        @Query(value = "user_id") userId: Long,
    ): Call<List<AchievementRemote>>
}
