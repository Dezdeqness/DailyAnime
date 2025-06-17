package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.FilterItem
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.model.FilterType
import com.dezdeqness.domain.model.GenreEntity
import com.dezdeqness.domain.model.GenreKindEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterMapper @Inject constructor() {

    fun fromResponse(item: FilterItem) =
        FilterEntity(
            id = item.id,
            name = item.name,
            type = FilterType.fromString(item.type)
        )

    fun fromResponse(item: GenreEntity) =
        FilterEntity(
            id = item.id,
            name = item.name,
            type = when (item.kind) {
                GenreKindEntity.DEMOGRAPHIC -> FilterType.AUDIENCE
                GenreKindEntity.THEME -> FilterType.THEME
                else -> FilterType.GENRE
            }
        )
}
