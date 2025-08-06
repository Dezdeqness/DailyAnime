package com.dezdeqness.feature.achievements.presentation.models

data class AchievementsUiModel(
    val id: String,
    val titleRu: String,
    val titleEn: String,
    val level: Int,
    val progress: Long,
    val imageUrl: String,
)
