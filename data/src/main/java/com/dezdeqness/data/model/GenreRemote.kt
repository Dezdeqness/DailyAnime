package com.dezdeqness.data.model

import com.squareup.moshi.Json

data class GenreRemote(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "russian")
    val russian: String,
    @field:Json(name = "entry_type")
    val type: String,
)
