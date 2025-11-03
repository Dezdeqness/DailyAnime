package com.dezdeqness.data.model.favorite

import com.squareup.moshi.Json

data class FavoritesResponse(
    @field:Json(name = "animes")
    val animes: List<FavoriteItemEntity>,
    @field:Json(name = "mangas")
    val mangas: List<FavoriteItemEntity>,
    @field:Json(name = "ranobe")
    val ranobe: List<FavoriteItemEntity>,
    @field:Json(name = "characters")
    val characters: List<FavoriteItemEntity>,
    @field:Json(name = "people")
    val people: List<FavoriteItemEntity>,
    @field:Json(name = "mangakas")
    val mangakas: List<FavoriteItemEntity>,
    @field:Json(name = "seyu")
    val seyu: List<FavoriteItemEntity>,
    @field:Json(name = "producers")
    val producers: List<FavoriteItemEntity>,
)
