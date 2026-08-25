package com.dezdeqness.feature.achievements.data

import com.dezdeqness.contract.achievements.model.AchievementConfigEntity
import com.dezdeqness.contract.achievements.model.AchievementEntity
import com.dezdeqness.data.util.TimestampConverter
import javax.inject.Inject

internal class AchievementMapper @Inject constructor(
    private val timestampConverter: TimestampConverter,
) {
    fun fromResponse(item: AchievementRemote) = AchievementEntity(
        id = item.id,
        nekoId = item.nekoId,
        level = item.level,
        progress = item.progress,
        userId = item.userId,
        createdAtTimestamp = timestampConverter.convertToTimeStampWithTime(item.createdAt),
        updatedAtTimestamp = timestampConverter.convertToTimeStampWithTime(item.updatedAt),
    )

    fun fromResponse(item: AchievementConfigRemote) = AchievementConfigEntity(
        nekoId = item.nekoId,
        level = item.level,
        titleRu = item.metadata.titleRu,
        textRu = item.metadata.textRu,
        titleEn = item.metadata.titleEn,
        textEn = item.metadata.textEn,
        image = item.metadata.image,
    )
}
