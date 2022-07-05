package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class AnimeAPI(

    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "russian")
    val russian: String,

    @field:Json(name = "image")
    val images: Map<String, String>?,

    @field:Json(name = "url")
    val url: String,

    @field:Json(name = "kind")
    val kind: String,

    @field:Json(name = "score")
    val score: String,

    @field:Json(name = "status")
    val status: String,

    @field:Json(name = "episodes")
    val episodes: Int,

    @field:Json(name = "episodes_aired")
    val episodesAired: Int,

    @field:Json(name = "aired_on")
    val airedOn: String?,

    @field:Json(name = "released_on")
    val releasedOn: String?,
) : DataModel.Api
