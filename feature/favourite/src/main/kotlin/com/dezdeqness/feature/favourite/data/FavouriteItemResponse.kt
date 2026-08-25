package com.dezdeqness.feature.favourite.data

import com.squareup.moshi.Json

internal data class FavouriteItemResponse(
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
