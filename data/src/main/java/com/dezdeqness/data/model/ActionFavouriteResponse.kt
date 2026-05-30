package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class ActionFavouriteResponse(
    @field:Json(name = "success")
    val success: Boolean,

    @field:Json(name = "notice")
    val notice: String,
) : DataModel.Api
