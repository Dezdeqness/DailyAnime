package com.dezdeqness.feature.details.related.presentation.mapper

import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.Entity
import com.dezdeqness.feature.details.related.presentation.RelatedListUiMapper
import com.dezdeqness.feature.details.related.presentation.models.ChronologyUiModel
import com.dezdeqness.feature.details.related.presentation.models.RelatedListItem
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import javax.inject.Inject

class ChronologyUiMapper @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
) : RelatedListUiMapper {

    override fun map(item: Entity): RelatedListItem? {
        if (item !is AnimeChronologyEntity) return null

        return ChronologyUiModel(
            id = item.id,
            name = item.name,
            imageUrl = item.imageUrl.replace(IMAGE_QUALITY_X96, IMAGE_QUALITY_ORIGINAL),
            briefInfo = createBriefInfoOrEmpty(item = item),
        )
    }

    private fun createBriefInfoOrEmpty(item: AnimeChronologyEntity): String {
        val builder = StringBuilder()

        if (item.kind.isNotEmpty()) {
            val animeKind = AnimeKind.fromString(item.kind)
            val kindString = animeKindUtils.mapKind(animeKind)
            if (kindString.isNotEmpty()) {
                builder.append(kindString)
            }
        }

        if (item.year.isNotEmpty()) {
            if (builder.isNotEmpty()) {
                builder.append(SEPARATOR)
            }
            builder.append(item.year)
        }

        return builder.toString()
    }

    companion object {
        private const val SEPARATOR = " • "
        private const val IMAGE_QUALITY_X96 = "x96"
        private const val IMAGE_QUALITY_ORIGINAL = "original"
    }
}
