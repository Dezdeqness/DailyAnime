package com.dezdeqness.contract.achievements.repository

import com.dezdeqness.contract.achievements.model.AchievementEntity

interface AchievementRepository {
    fun fetchAchievementsByUserId(id: Long): Result<List<AchievementEntity>>
}
