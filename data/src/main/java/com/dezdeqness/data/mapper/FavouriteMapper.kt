package com.dezdeqness.data.mapper

import com.dezdeqness.contract.favourite.model.FavouriteEntity
import com.dezdeqness.contract.favourite.model.FavouriteType
import com.dezdeqness.data.model.favorite.FavouriteItemResponse
import com.dezdeqness.data.model.favorite.FavouritesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteMapper @Inject constructor() {

    fun fromResponse(favorite: FavouritesResponse): List<FavouriteEntity> {
        val result = mutableListOf<FavouriteEntity>()

        result += favorite.animes.map { fromResponse(it, FavouriteType.ANIME) }
        result += favorite.mangas.map { fromResponse(it, FavouriteType.MANGA) }
        result += favorite.ranobe.map { fromResponse(it, FavouriteType.RANOBE) }
        result += favorite.characters.map { fromResponse(it, FavouriteType.CHARACTER) }
        result += favorite.people.map { fromResponse(it, FavouriteType.PERSON) }
        result += favorite.mangakas.map { fromResponse(it, FavouriteType.MANGAKA) }
        result += favorite.seyu.map { fromResponse(it, FavouriteType.SEYU) }
        result += favorite.producers.map { fromResponse(it, FavouriteType.PRODUCER) }

        return result
    }

    fun fromResponse(favourite: FavouriteItemResponse, favouriteType: FavouriteType): FavouriteEntity =
        FavouriteEntity(
            id = favourite.id,
            name = favourite.name,
            russian = favourite.russian,
            image = favourite.image,
            url = favourite.url,
            type = favouriteType
        )

}
