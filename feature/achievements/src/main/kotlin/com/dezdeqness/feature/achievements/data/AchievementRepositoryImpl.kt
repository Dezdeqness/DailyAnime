package com.dezdeqness.feature.achievements.data

import com.dezdeqness.contract.achievements.repository.AchievementRepository
import javax.inject.Inject

internal class AchievementRepositoryImpl @Inject constructor(
    private val achievementRemoteDataSource: AchievementRemoteDataSource,
) : AchievementRepository {
    override fun fetchAchievementsByUserId(id: Long) =
        achievementRemoteDataSource.getAchievementList(userId = id)
}
