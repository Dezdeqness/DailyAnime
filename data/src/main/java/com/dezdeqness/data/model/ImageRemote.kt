package com.dezdeqness.data.model

import com.squareup.moshi.Json

class ImageRemote {

    @field:Json(name = "original")
    val original: String = ""

    @field:Json(name = "preview")
    val preview: String = ""

    @field:Json(name = "x96")
    val x96: String = ""

    @field:Json(name = "x48")
    val x48: String = ""
}
