package com.dezdeqness.data.mapper

import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.model.RelatedItemRemote
import com.dezdeqness.contract.anime.model.RelatedItemEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelatedItemMapper @Inject constructor(
    private val animeMapper: AnimeMapper,
) {

    fun fromResponse(item: RelatedItemRemote): RelatedItemEntity? {
        if (item.anime == null) {
            return null
        }
        return RelatedItemEntity(
            relationTitle = item.relation,
            relationTitleRussian = item.relationRussian,
            animeBriefEntity = animeMapper.fromResponse(item.anime)
        )
    }

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
