package com.dezdeqness.contract.favourite.model

data class FavouriteEntity(
    val id: Long,
    val name: String,
    val russian: String?,
    val image: String?,
    val url: String?,
    val type: FavouriteType
)

enum class FavouriteType {
    ANIME,
    MANGA,
    RANOBE,
    CHARACTER,
    PERSON,
    MANGAKA,
    SEYU,
    PRODUCER
}
