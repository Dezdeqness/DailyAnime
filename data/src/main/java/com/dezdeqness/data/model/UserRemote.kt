package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class UserRemote(
    val id: Long,
    val nickname: String,
    val avatar: String,
    @field:Json(name = "image")
    val image: ImageRemote? = null,
    @field:Json("last_online_at")
    val lastOnlineAt: String,
    val url: String,
) : DataModel.Api
