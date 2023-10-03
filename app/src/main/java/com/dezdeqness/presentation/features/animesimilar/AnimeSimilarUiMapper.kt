package com.dezdeqness.presentation.features.animesimilar

import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import com.dezdeqness.presentation.models.SimilarUiModel
import com.dezdeqness.utils.ImageUrlUtils
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class AnimeSimilarUiMapper @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
) {

    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    fun map(item: AnimeBriefEntity): SimilarUiModel {
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
            stringBuilder.append(item.kind.kind)
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
