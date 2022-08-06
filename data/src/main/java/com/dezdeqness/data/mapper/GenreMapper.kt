package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.GenreRemote
import com.dezdeqness.domain.model.GenreEntity
import javax.inject.Inject

class GenreMapper @Inject constructor() {

    fun fromResponse(item: GenreRemote) =
        GenreEntity(
            id = "${item.id}-${item.name}",
            name = item.russian,
            type = item.kind
        )

}
