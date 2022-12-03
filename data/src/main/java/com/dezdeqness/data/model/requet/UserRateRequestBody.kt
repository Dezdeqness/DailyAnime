package com.dezdeqness.data.model.requet

import com.squareup.moshi.Json

data class UserRateRequestBody(
    @field:Json(name = "user_rate")
    val userRate: UserRate
)

data class UserRate (
    val chapters: String,
    val episodes: String,
    val rewatches: String,
    val score: String,
    val status: String,
    val text: String,
    val volumes: String
)
