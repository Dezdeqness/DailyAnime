package com.dezdeqness.feature.achievements.presentation

import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.domain.model.AchievementConfigEntity
import com.dezdeqness.domain.model.AchievementEntity
import com.dezdeqness.feature.achievements.presentation.models.AchievementsUiModel
import javax.inject.Inject

class AchievementsComposer @Inject constructor(
    private val configManager: ConfigManager,
) {

    fun compose(
        configData: Map<String, List<AchievementConfigEntity>>,
        achievements: List<AchievementEntity>,
    ): List<AchievementsUiModel> {
        return achievements
            .groupBy { it.nekoId }
            .mapNotNull { (nekoId, userAchievements) ->
                val maxAchievement =
                    userAchievements.maxByOrNull { it.level } ?: return@mapNotNull null
                val configForId = configData[nekoId] ?: return@mapNotNull null
                val configLevel =
                    configForId.find { it.level == maxAchievement.level } ?: return@mapNotNull null

                AchievementsUiModel(
                    id = nekoId,
                    titleRu = configLevel.titleRu,
                    titleEn = configLevel.titleEn,
                    level = maxAchievement.level,
                    progress = maxAchievement.progress / 100f,
                    progressValue = maxAchievement.progress,
                    imageUrl = configManager.baseUrl + configLevel.image
                )
            }
    }
}
