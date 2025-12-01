package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.anime.model.GenreKindEntity
import com.dezdeqness.contract.anime.model.TypeEntity
import com.dezdeqness.data.AnimeDetailsQuery
import com.dezdeqness.data.model.GenreRemote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreMapper @Inject constructor() {

    fun fromResponse(item: GenreRemote) =
        GenreEntity(
            numericId = item.id.toString(),
            id = "${item.id}-${item.name}",
            name = item.russian,
            type = TypeEntity.fromString(item.type),
            kind = GenreKindEntity.fromString(item.kind),
        )

    fun fromResponseGraphql(item: AnimeDetailsQuery.Genre) =
        GenreEntity(
            numericId = item.id,
            id = "${item.id}-${item.name}",
            name = item.russian,
            type = TypeEntity.fromString(item.entryType.rawValue),
            kind = GenreKindEntity.fromString(item.kind.rawValue),
        )

}
