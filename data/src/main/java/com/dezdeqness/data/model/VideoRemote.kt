package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class VideoRemote(

    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "name")
    val name: String?,

    @field:Json(name = "player_url")
    val playerUrl: String,

    @field:Json(name = "url")
    val url: String,

    @field:Json(name = "image_url")
    val imageUrl: String,

    @field:Json(name = "kind")
    val kind: String,

    @field:Json(name = "hosting")
    val hosting: String,
) : DataModel.Api
