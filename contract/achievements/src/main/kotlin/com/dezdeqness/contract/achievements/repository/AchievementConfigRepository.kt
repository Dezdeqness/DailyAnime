package com.dezdeqness.contract.achievements.repository

import com.dezdeqness.contract.achievements.model.AchievementConfigDataEntity

interface AchievementConfigRepository {
    fun getConfig(): AchievementConfigDataEntity
}
