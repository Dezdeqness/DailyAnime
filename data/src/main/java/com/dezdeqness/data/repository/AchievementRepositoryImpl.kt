package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AchievementRemoteDataSource
import com.dezdeqness.domain.repository.AchievementRepository

class AchievementRepositoryImpl(
    private val achievementRemoteDataSource: AchievementRemoteDataSource,
) : AchievementRepository {
    override fun fetchAchievementsByUserId(id: Long) =
        achievementRemoteDataSource.getAchievementList(userId = id)

}
