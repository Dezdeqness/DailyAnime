package com.dezdeqness.data.model

import com.squareup.moshi.Json

data class AccountRemote(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "nickname")
    val nickname: String,

    @field:Json(name = "avatar")
    val avatar: String,

    @field:Json(name = "image")
    val images: Map<String, String>,

    @field:Json(name = "last_online_at")
    val lastOnline: String,

    @field:Json(name = "name")
    val name: String?,

    @field:Json(name = "sex")
    val sex: String,
)
