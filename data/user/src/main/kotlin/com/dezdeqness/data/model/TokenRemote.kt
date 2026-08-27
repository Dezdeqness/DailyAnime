package com.dezdeqness.data.model

import com.squareup.moshi.Json

data class TokenRemote(

    @field:Json(name = "access_token")
    val accessToken: String,

    @field:Json(name = "refresh_token")
    val refreshToken: String,

    @field:Json(name = "expires_in")
    val expiresIn: Long,

    @field:Json(name = "created_at")
    val createdAt: Long,
)
