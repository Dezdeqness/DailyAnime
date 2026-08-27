package com.dezdeqness.feature.userrate.data.model

import com.squareup.moshi.Json

internal data class UpdateUserRateRequestBody(
    @field:Json(name = "user_rate") val userRate: UpdateUserRate,
)

internal data class UpdateUserRate(
    val chapters: String,
    val episodes: String,
    val rewatches: String,
    val score: String,
    val status: String,
    val text: String,
    val volumes: String,
)

internal data class PostUserRateRequestBody(
    @field:Json(name = "user_rate") val userRate: PostUserRate,
)

internal data class PostUserRate(
    val chapters: String,
    val episodes: String,
    val rewatches: String,
    val score: String,
    val status: String,
    val text: String,
    val volumes: String,
    @field:Json(name = "user_id") val userId: Long,
    @field:Json(name = "target_id") val targetId: String,
    @field:Json(name = "target_type") val targetType: String,
)
