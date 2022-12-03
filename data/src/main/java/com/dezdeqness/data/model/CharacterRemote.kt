package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class CharacterRemote(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "russian")
    val russian: String,

    @field:Json(name = "image")
    val image: ImageRemote,

    @field:Json(name = "url")
    val url: String
) : DataModel.Api
