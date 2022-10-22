package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.RelatedItemRemote
import com.dezdeqness.domain.model.RelatedItemEntity
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
            animeEntity = animeMapper.fromResponse(item.anime)
        )
    }


}
