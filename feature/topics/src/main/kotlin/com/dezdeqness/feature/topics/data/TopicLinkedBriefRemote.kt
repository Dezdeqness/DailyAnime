package com.dezdeqness.feature.topics.data

import com.dezdeqness.data.model.ImageRemote
import com.squareup.moshi.Json

internal class TopicLinkedBriefRemote {

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
}
