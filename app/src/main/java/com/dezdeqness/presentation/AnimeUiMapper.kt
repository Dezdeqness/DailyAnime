package com.dezdeqness.presentation

import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.RelatedItemEntity
import com.dezdeqness.presentation.models.AnimeUiModel
import com.dezdeqness.presentation.models.RelatedItemUiModel
import com.dezdeqness.utils.ImageUrlUtils
import javax.inject.Inject

class AnimeUiMapper @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
) {

    fun map(animeBriefEntity: AnimeBriefEntity) =
        AnimeUiModel(
            id = animeBriefEntity.id,
            briefInfo = "${animeBriefEntity.name}\n${animeBriefEntity.status}",
            kind = animeBriefEntity.kind.name,
            logoUrl = imageUrlUtils.getImageWithBaseUrl(animeBriefEntity.image.preview),
            airedYear = animeBriefEntity.airedOn.split("-").firstOrNull().orEmpty()
        )

    fun map(relatedItemUiModel: RelatedItemEntity) =
        RelatedItemUiModel(
            title = relatedItemUiModel.relationTitleRussian,
            anime = map(relatedItemUiModel.animeBriefEntity)
        )

}
