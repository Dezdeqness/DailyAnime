package com.dezdeqness.presentation

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.RelatedItemEntity
import com.dezdeqness.presentation.features.animelist.AnimeUiModel
import com.dezdeqness.presentation.models.RelatedItemUiModel
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class AnimeUiMapper @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
) {

    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    fun map(items: List<AnimeBriefEntity>) = items.map(::mapAnimeBrief)

    fun mapAnimeBrief(animeBriefEntity: AnimeBriefEntity) = AnimeUiModel(
        id = animeBriefEntity.id,
        title = animeBriefEntity.takeIf { it.russian.isNotEmpty() }?.russian
            ?: animeBriefEntity.name,
        kind = animeKindUtils.mapKind(animeBriefEntity.kind),
        logoUrl = animeBriefEntity.image.original,
    )

    fun map(relatedItemUiModel: RelatedItemEntity) = RelatedItemUiModel(
        id = relatedItemUiModel.animeBriefEntity.id,
        type = relatedItemUiModel.takeIf { it.relationTitleRussian.isNotEmpty() }?.relationTitleRussian
            ?: relatedItemUiModel.relationTitle,
        briefInfo =
        animeKindUtils.mapKind(relatedItemUiModel.animeBriefEntity.kind) +
            " • " +
            yearFormatter.format(relatedItemUiModel.animeBriefEntity.airedOnTimestamp),
        logoUrl = relatedItemUiModel.animeBriefEntity.image.original,
    )
}
