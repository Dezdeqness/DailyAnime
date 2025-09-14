package com.dezdeqness.presentation.features.animesimilar

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.contract.anime.model.Entity
import com.dezdeqness.data.utils.ImageUrlUtils
import com.dezdeqness.presentation.features.genericlistscreen.GenericListableUiMapper
import com.dezdeqness.presentation.models.SimilarUiModel
import com.dezdeqness.utils.AnimeKindUtils
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class AnimeSimilarUiMapper @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
    private val animeKindUtils: AnimeKindUtils,
) : GenericListableUiMapper {

    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    override fun map(item: Entity): SimilarUiModel? {
        if (item !is AnimeBriefEntity) return null
        val stringBuilder = StringBuilder()

        if (item.status == AnimeStatus.ONGOING || item.status == AnimeStatus.ANONS) {
            stringBuilder.append(item.status.status)
        }

        if (item.airedOnTimestamp != 0L) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(SEPARATOR)
            }
            stringBuilder.append(yearFormatter.format(item.airedOnTimestamp))
        }

        if (item.kind != AnimeKind.UNKNOWN) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(SEPARATOR)
            }
            stringBuilder.append(animeKindUtils.mapKind(item.kind))
        }

        if (stringBuilder.isNotEmpty()) {
            stringBuilder.append(SEPARATOR)
        }
        stringBuilder.append(
            "" + item.episodes + " эп."
        )

        return SimilarUiModel(
            id = item.id,
            name = item.russian,
            briefInfo = stringBuilder.toString(),
            score = item.score.toString(),
            logoUrl = imageUrlUtils.getImageWithBaseUrl(item.image.preview),
        )
    }

    companion object {
        private const val SEPARATOR = " • "
    }
}
