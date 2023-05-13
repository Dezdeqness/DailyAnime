package com.dezdeqness.data.model

import com.squareup.moshi.Json

class AnimeChronologyRemote : AnimeBriefRemote() {

    @field:Json(name = "year")
    val year: String? = null

    @field:Json(name = "image_url")
    val imageUrl: String = ""

}

class AnimeChronologyResponseRemote {

    @field:Json(name = "nodes")
    val nodes: List<AnimeChronologyRemote>? = null
}
