package com.dezdeqness.domain.model

data class TokenEntity(
    val accessToken: String,
    val refreshToken: String,
    val createdIn: Long,
    val expiresIn: Long,
)
