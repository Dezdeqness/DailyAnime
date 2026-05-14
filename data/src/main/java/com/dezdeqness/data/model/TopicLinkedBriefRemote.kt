package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

class TopicLinkedBriefRemote : DataModel.Api {

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
