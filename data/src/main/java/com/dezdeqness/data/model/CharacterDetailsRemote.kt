package com.dezdeqness.data.model

import com.dezdeqness.data.core.DataModel
import com.squareup.moshi.Json

data class CharacterDetailsRemote(
    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "russian")
    val russian: String,

    @field:Json(name = "image")
    val image: ImageRemote,

    @field:Json(name = "url")
    val url: String,

    @field:Json(name = "description")
    val description: String,

    @field:Json(name = "description_html")
    val descriptionHtml: String,

    @field:Json(name = "seyu")
    val seyuList: List<CharacterRemote>,
    @field:Json(name = "animes")
    val animeList: List<AnimeBriefRemote>,
) : DataModel.Api
