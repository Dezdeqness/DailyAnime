package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AchievementEntity

interface AchievementRepository {
    fun fetchAchievementsByUserId(id: Long): Result<List<AchievementEntity>>
}
