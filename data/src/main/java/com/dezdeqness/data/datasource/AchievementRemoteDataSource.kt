package com.dezdeqness.data.datasource

import com.dezdeqness.contract.achievements.model.AchievementEntity

interface AchievementRemoteDataSource {
    fun getAchievementList(userId: Long): Result<List<AchievementEntity>>
}
