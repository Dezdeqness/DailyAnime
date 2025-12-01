package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.StudioEntity
import com.dezdeqness.data.AnimeDetailsQuery
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StudioMapper @Inject constructor() {

    fun fromResponseGraphql(item: AnimeDetailsQuery.Studio) =
        StudioEntity(
            id = item.id.toInt(),
            name = item.name,
            filteredName = item.name,
            real = true,
        )

}
