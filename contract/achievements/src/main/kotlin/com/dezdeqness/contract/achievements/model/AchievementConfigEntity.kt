package com.dezdeqness.contract.achievements.model

data class AchievementConfigEntity(
    val nekoId: String,
    val level: Int,
    val titleRu: String,
    val textRu: String?,
    val titleEn: String,
    val textEn: String?,
    val image: String,
)
