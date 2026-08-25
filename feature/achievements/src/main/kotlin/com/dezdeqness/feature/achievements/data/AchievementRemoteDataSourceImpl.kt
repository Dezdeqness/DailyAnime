package com.dezdeqness.feature.achievements.data

import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import dagger.Lazy
import javax.inject.Inject

internal class AchievementRemoteDataSourceImpl @Inject constructor(
    private val achievementApiService: Lazy<AchievementApiService>,
    private val achievementMapper: AchievementMapper,
) : AchievementRemoteDataSource, BaseDataSource() {
    override fun getAchievementList(userId: Long) = tryWithCatch {
        val response = achievementApiService.get().getAchievementList(userId = userId).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                responseBody.map(achievementMapper::fromResponse),
            )
        } else {
            throw response.createApiException()
        }
    }
}
