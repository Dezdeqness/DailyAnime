package com.dezdeqness.data.model

import com.squareup.moshi.Json

data class HistoryRemote(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "description")
    val description: String,

    @field:Json(name = "created_at")
    val createdAt: String,

    @field:Json(name = "target")
    val historyItem: HistoryItem? = null,
)

data class HistoryItem(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "russian")
    val russian: String,

    @field:Json(name = "url")
    val url: String,

    @field:Json(name = "image")
    val imageRemote: ImageRemote,
)
