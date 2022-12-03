package com.dezdeqness.domain.model

data class UserRateEntity(
    val id: Long,
    val score: Long,
    val status: String,
    val text: String,
    val episodes: Long,
    val chapters: Long,
    val volumes: Long,
    val textHTML: String,
    val rewatches: Long,
    val createdAt: String,
    val updatedAt: String,
    val anime: AnimeBriefEntity? = null
)
