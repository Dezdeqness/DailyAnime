package com.dezdeqness.data.model.requet

import com.squareup.moshi.Json

data class UpdateUserRateRequestBody(
    @field:Json(name = "user_rate") val userRate: UpdateUserRate
)

data class UpdateUserRate(
    val chapters: String,
    val episodes: String,
    val rewatches: String,
    val score: String,
    val status: String,
    val text: String,
    val volumes: String
)

data class PostUserRateRequestBody(
    @field:Json(name = "user_rate") val userRate: PostUserRate
)

data class PostUserRate(
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
