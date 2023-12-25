package com.dezdeqness.presentation.features.animechronology

import com.dezdeqness.domain.model.AnimeChronologyEntity
import com.dezdeqness.domain.model.Entity
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableUiMapper
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.ChronologyUiModel
import javax.inject.Inject

class AnimeChronologyUiMapper @Inject constructor() : GenericListableUiMapper {

    override fun map(item: Entity): AdapterItem? {
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
            builder.append(item.kind)
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
