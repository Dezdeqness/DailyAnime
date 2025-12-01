package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.data.DetailsQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelatedItemMapper @Inject constructor(
    private val animeMapper: AnimeMapper,
) {

    fun fromResponse(item: DetailsQuery.Related): RelatedItemEntity? {
        if (item.anime == null) {
            return null
        }

        return RelatedItemEntity(
            relationTitle = item.relationKind.name,
            relationTitleRussian = item.relationText,
            animeBriefEntity = animeMapper.fromResponse(item.anime)
        )
    }



}
