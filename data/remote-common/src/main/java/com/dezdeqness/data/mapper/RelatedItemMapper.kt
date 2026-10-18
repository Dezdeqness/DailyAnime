package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.contract.anime.model.RelationKind
import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.type.RelationKindEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelatedItemMapper @Inject constructor(
    private val animeMapper: AnimeMapper,
) {

    fun fromResponse(item: DetailsQuery.Related): RelatedItemEntity? {
        val anime = item.anime ?: return null

        return RelatedItemEntity(
            kind = relationKinds[item.relationKind] ?: RelationKind.OTHER,
            animeBriefEntity = animeMapper.fromResponse(anime),
        )
    }

    companion object {
        private val relationKinds = mapOf(
            RelationKindEnum.adaptation to RelationKind.ADAPTATION,
            RelationKindEnum.alternative_setting to RelationKind.ALTERNATIVE_SETTING,
            RelationKindEnum.alternative_version to RelationKind.ALTERNATIVE,
            RelationKindEnum.character to RelationKind.CHARACTER,
            RelationKindEnum.full_story to RelationKind.FULL_STORY,
            RelationKindEnum.parent_story to RelationKind.PARENT,
            RelationKindEnum.prequel to RelationKind.PREQUEL,
            RelationKindEnum.sequel to RelationKind.SEQUEL,
            RelationKindEnum.side_story to RelationKind.SIDE_STORY,
            RelationKindEnum.spin_off to RelationKind.SPIN_OFF,
            RelationKindEnum.summary to RelationKind.SUMMARY,
        )
    }
}
