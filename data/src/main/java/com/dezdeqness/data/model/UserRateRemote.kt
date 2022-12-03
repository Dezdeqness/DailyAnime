package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

class UserRateRemote : DataModel.Api {

    @field:Json(name = "id")
    val id: Long = 0

    @field:Json(name = "score")
    val score: Long = 0

    @field:Json(name = "status")
    val status: String = ""

    @field:Json(name = "text")
    val text: String? = null

    @field:Json(name = "episodes")
    val episodes: Long? = null

    @field:Json(name = "chapters")
    val chapters: Long? = null

    @field:Json(name = "volumes")
    val volumes: Long? = null

    @field:Json(name = "text_html")
    val textHTML: String = ""

    @field:Json(name = "rewatches")
    val rewatches: Long = 0

    @field:Json(name = "created_at")
    val createdAt: String = ""

    @field:Json(name = "updated_at")
    val updatedAt: String = ""

    @field:Json(name = "anime")
    val anime: AnimeBriefRemote? = null

}
