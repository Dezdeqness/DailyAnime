package com.dezdeqness.data.model.favorite

import com.squareup.moshi.Json

data class FavouritesResponse(
    @field:Json(name = "animes")
    val animes: List<FavouriteItemResponse>,
    @field:Json(name = "mangas")
    val mangas: List<FavouriteItemResponse>,
    @field:Json(name = "ranobe")
    val ranobe: List<FavouriteItemResponse>,
    @field:Json(name = "characters")
    val characters: List<FavouriteItemResponse>,
    @field:Json(name = "people")
    val people: List<FavouriteItemResponse>,
    @field:Json(name = "mangakas")
    val mangakas: List<FavouriteItemResponse>,
    @field:Json(name = "seyu")
    val seyu: List<FavouriteItemResponse>,
    @field:Json(name = "producers")
    val producers: List<FavouriteItemResponse>,
)
