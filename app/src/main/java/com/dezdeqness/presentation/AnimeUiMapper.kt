package com.dezdeqness.presentation

import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.RelatedItemEntity
import com.dezdeqness.presentation.models.AnimeUiModel
import com.dezdeqness.presentation.models.RelatedItemUiModel
import com.dezdeqness.utils.ImageUrlUtils
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class AnimeUiMapper @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
) {

    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    fun map(items: List<AnimeBriefEntity>) = items.map(::mapAnimeBrief)

    fun mapAnimeBrief(animeBriefEntity: AnimeBriefEntity) =
        AnimeUiModel(
            id = animeBriefEntity.id,
            title = animeBriefEntity.takeIf { it.russian.isNotEmpty() }?.russian
                ?: animeBriefEntity.name,
            kind = animeBriefEntity.kind.name,
            logoUrl = imageUrlUtils.getImageWithBaseUrl(animeBriefEntity.image.original),
        )

    fun map(relatedItemUiModel: RelatedItemEntity) =
        RelatedItemUiModel(
            id = relatedItemUiModel.animeBriefEntity.id,
            type = relatedItemUiModel.takeIf { it.relationTitleRussian.isNotEmpty() }?.relationTitleRussian
                ?: relatedItemUiModel.relationTitle,
            briefInfo =
            relatedItemUiModel.animeBriefEntity.kind.name
                    + " • "
                    + yearFormatter.format(relatedItemUiModel.animeBriefEntity.airedOnTimestamp),
            logoUrl = imageUrlUtils.getImageWithBaseUrl(relatedItemUiModel.animeBriefEntity.image.original),
        )

}
