package com.dezdeqness.domain.model

data class AchievementEntity(
    val id: Long,
    val nekoId: String,
    val level: Int,
    val progress: Long,
    val userId: Long,
    val createdAtTimestamp: Long,
    val updatedAtTimestamp: Long,
)
