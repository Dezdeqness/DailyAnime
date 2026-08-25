package com.dezdeqness.feature.achievements.data

import com.squareup.moshi.Json

internal data class AchievementConfigRemote(
    @field:Json(name = "neko_id")
    val nekoId: String,

    @field:Json(name = "level")
    val level: Int,

    @field:Json(name = "metadata")
    val metadata: AchievementMetadataRemote,
)

internal data class AchievementMetadataRemote(
    @field:Json(name = "title_ru")
    val titleRu: String,

    @field:Json(name = "text_ru")
    val textRu: String?,

    @field:Json(name = "title_en")
    val titleEn: String,

    @field:Json(name = "text_en")
    val textEn: String?,

    val image: String,
)
