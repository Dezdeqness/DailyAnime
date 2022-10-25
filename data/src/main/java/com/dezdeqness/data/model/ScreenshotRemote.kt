package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class ScreenshotRemote(

    @field:Json(name = "original")
    val original: String,

    @field:Json(name = "preview")
    val preview: String,
) : DataModel.Api
