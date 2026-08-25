package com.dezdeqness.feature.favourite.data

import com.squareup.moshi.Json

internal data class ActionFavouriteResponse(
    @field:Json(name = "success")
    val success: Boolean,

    @field:Json(name = "notice")
    val notice: String,
)
