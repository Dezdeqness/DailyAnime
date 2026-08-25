package com.dezdeqness.feature.topics.data

import com.dezdeqness.data.model.ImageRemote
import com.squareup.moshi.Json

class UserRemote(
    val id: Long,
    val nickname: String,
    val avatar: String,
    @field:Json(name = "image")
    val image: ImageRemote? = null,
    @field:Json("last_online_at")
    val lastOnlineAt: String,
    val url: String,
)
