package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class AnimeAPI(

    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "russian")
    val russian: String,

    @Json(name = "image")
    val images: Map<String, String>?,

    @Json(name = "url")
    val url: String,

    @Json(name = "kind")
    val kind: String,

    @Json(name = "score")
    val score: String,

    @Json(name = "status")
    val status: String,

    @Json(name = "episodes")
    val episodes: Int,

    @Json(name = "episodes_aired")
    val episodesAired: Int,

    @Json(name = "aired_on")
    val airedOn: String?,

    @Json(name = "released_on")
    val releasedOn: String?,
) : DataModel.Api
