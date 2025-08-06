package com.dezdeqness.data.mapper

import com.dezdeqness.data.core.TimestampConverter
import com.dezdeqness.data.model.AchievementConfigRemote
import com.dezdeqness.data.model.AchievementRemote
import com.dezdeqness.domain.model.AchievementConfigEntity
import com.dezdeqness.domain.model.AchievementEntity
import javax.inject.Inject

class AchievementMapper @Inject constructor(
    private val timestampConverter: TimestampConverter,
) {
    fun fromResponse(item: AchievementRemote) =
        AchievementEntity(
            id = item.id,
            nekoId = item.nekoId,
            level = item.level,
            progress = item.progress,
            userId = item.userId,
            createdAtTimestamp = timestampConverter.convertToTimeStampWithTime(item.createdAt),
            updatedAtTimestamp = timestampConverter.convertToTimeStampWithTime(item.updatedAt),

        )

    fun fromResponse(item: AchievementConfigRemote) =
        AchievementConfigEntity(
            nekoId = item.nekoId,
            level = item.level,
            titleRu = item.metadata.titleRu,
            textRu = item.metadata.textRu,
            titleEn = item.metadata.titleEn,
            textEn = item.metadata.textEn,
            image = item.metadata.image,
        )
}
