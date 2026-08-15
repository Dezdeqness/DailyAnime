package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.anime.model.GenreKindEntity
import com.dezdeqness.contract.filter.model.FilterEntity
import com.dezdeqness.contract.filter.model.FilterType
import com.dezdeqness.data.model.FilterItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterMapper @Inject constructor() {

    fun fromResponse(item: FilterItem) = FilterEntity(
        id = item.id,
        name = item.name,
        type = FilterType.fromString(item.type),
    )

    fun fromResponse(item: GenreEntity) = FilterEntity(
        id = item.id,
        name = item.name,
        type = when (item.kind) {
            GenreKindEntity.DEMOGRAPHIC -> FilterType.AUDIENCE
            GenreKindEntity.THEME -> FilterType.THEME
            else -> FilterType.GENRE
        },
    )
}
