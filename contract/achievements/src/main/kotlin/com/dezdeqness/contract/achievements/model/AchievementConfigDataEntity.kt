package com.dezdeqness.contract.achievements.model

data class AchievementConfigDataEntity(
    val common: Map<String, List<AchievementConfigEntity>> = mapOf(),
    val genres: Map<String, List<AchievementConfigEntity>> = mapOf(),
)
