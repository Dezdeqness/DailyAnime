package com.dezdeqness.data.datasource

import com.dezdeqness.data.AchievementService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.AchievementMapper
import dagger.Lazy
import javax.inject.Inject

class AchievementRemoteDataSourceImpl @Inject constructor(
    private val achievementService: Lazy<AchievementService>,
    private val achievementMapper: AchievementMapper,
) : AchievementRemoteDataSource, BaseDataSource() {
    override fun getAchievementList(userId: Long) = tryWithCatch {
        val response = achievementService.get().getAchievementList(userId = userId).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                responseBody.map(achievementMapper::fromResponse)
            )
        } else {
            throw response.createApiException()
        }

    }
}
