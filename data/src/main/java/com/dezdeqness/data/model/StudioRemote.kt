package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class StudioRemote(

    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "filtered_name")
    val filteredName: String,

    @field:Json(name = "real")
    val real: Boolean,
) : DataModel.Api
