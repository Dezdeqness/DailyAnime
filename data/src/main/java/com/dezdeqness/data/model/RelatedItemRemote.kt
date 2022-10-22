package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class RelatedItemRemote(
    @field:Json(name = "relation")
    val relation: String,

    @field:Json(name = "relation_russian")
    val relationRussian: String,

    @field:Json(name = "anime")
    val anime: AnimeBriefRemote? = null,
) : DataModel.Api
