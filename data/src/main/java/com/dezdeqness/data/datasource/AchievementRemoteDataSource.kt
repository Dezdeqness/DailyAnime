package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.AchievementEntity

interface AchievementRemoteDataSource {
    fun getAchievementList(userId: Long): Result<List<AchievementEntity>>
}
