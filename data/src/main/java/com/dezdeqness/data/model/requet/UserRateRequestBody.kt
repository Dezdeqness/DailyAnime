package com.dezdeqness.data.model.requet

import com.squareup.moshi.Json

data class UserRateRequestBody(
    @field:Json(name = "user_rate")
    val userRate: UpdateUserRate
)

open class UpdateUserRate(
    open val chapters: String,
    open val episodes: String,
    open val rewatches: String,
    open val score: String,
    open val status: String,
    open val text: String,
    open val volumes: String
)

data class PostUserRate(
    override val chapters: String,
    override val episodes: String,
    override val rewatches: String,
    override val score: String,
    override val status: String,
    override val text: String,
    override val volumes: String,
    @field:Json(name = "user_id")
    val userId: Long,
    @field:Json(name = "target_id")
    val targetId: String,
    @field:Json(name = "target_type")
    val targetType: String,
) : UpdateUserRate(
    chapters = chapters,
    episodes = episodes,
    rewatches = rewatches,
    score = score,
    status = status,
    text = text,
    volumes = episodes,
)