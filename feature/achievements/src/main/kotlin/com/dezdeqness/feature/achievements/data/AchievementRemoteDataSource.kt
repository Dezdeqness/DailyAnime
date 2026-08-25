package com.dezdeqness.feature.achievements.data

import com.dezdeqness.contract.achievements.model.AchievementEntity

internal interface AchievementRemoteDataSource {
    fun getAchievementList(userId: Long): Result<List<AchievementEntity>>
}
