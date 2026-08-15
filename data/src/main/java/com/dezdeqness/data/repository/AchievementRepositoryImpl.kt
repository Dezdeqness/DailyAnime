package com.dezdeqness.data.repository

import com.dezdeqness.contract.achievements.repository.AchievementRepository
import com.dezdeqness.data.datasource.AchievementRemoteDataSource
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor(
    private val achievementRemoteDataSource: AchievementRemoteDataSource,
) : AchievementRepository {
    override fun fetchAchievementsByUserId(id: Long) = achievementRemoteDataSource.getAchievementList(userId = id)
}
