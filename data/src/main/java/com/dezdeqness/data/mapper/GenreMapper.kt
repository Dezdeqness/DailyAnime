package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.GenreRemote
import com.dezdeqness.domain.model.GenreEntity
import com.dezdeqness.domain.model.GenreKindEntity
import com.dezdeqness.domain.model.TypeEntity
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

}
