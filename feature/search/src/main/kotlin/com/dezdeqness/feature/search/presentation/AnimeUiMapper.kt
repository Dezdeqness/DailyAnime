package com.dezdeqness.feature.search.presentation

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import javax.inject.Inject

class AnimeUiMapper @Inject constructor(
    private val animeKindUtils: AnimeKindUtils,
) {

    fun map(items: List<AnimeBriefEntity>) = items.map(::mapAnimeBrief)

    private fun mapAnimeBrief(animeBriefEntity: AnimeBriefEntity) = AnimeUiModel(
        id = animeBriefEntity.id,
        title = animeBriefEntity.takeIf { it.russian.isNotEmpty() }?.russian
            ?: animeBriefEntity.name,
        kind = animeKindUtils.mapKind(animeBriefEntity.kind),
        logoUrl = animeBriefEntity.image.original,
    )
}
