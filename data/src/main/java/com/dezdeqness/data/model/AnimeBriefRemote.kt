package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

open class AnimeBriefRemote : DataModel.Api {

    @field:Json(name = "id")
    val id: Long = 0

    @field:Json(name = "name")
    val name: String = ""

    @field:Json(name = "russian")
    val russian: String = ""

    @field:Json(name = "image")
    val image: ImageRemote? = null

    @field:Json(name = "url")
    val url: String = ""

    @field:Json(name = "kind")
    val kind: String? = null

    @field:Json(name = "score")
    val score: Float = 0.0f

    @field:Json(name = "status")
    val status: String = ""

    @field:Json(name = "episodes")
    val episodes: Int = 0

    @field:Json(name = "episodes_aired")
    val episodesAired: Int = 0

    @field:Json(name = "aired_on")
    val airedOn: String? = null

    @field:Json(name = "released_on")
    val releasedOn: String? = null
}
