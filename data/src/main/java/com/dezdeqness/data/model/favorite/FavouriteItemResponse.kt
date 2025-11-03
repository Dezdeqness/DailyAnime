package com.dezdeqness.data.model.favorite

import com.squareup.moshi.Json

data class FavouriteItemResponse(
    @field:Json(name = "id")
    val id: Long,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "russian")
    val russian: String? = null,
    @field:Json(name = "image")
    val image: String? = null,
    @field:Json(name = "url")
    val url: String? = null,
)
