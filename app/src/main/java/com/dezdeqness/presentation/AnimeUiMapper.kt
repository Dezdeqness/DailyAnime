package com.dezdeqness.presentation

import com.dezdeqness.domain.model.AnimeEntity
import com.dezdeqness.presentation.models.AnimeUiItem
import javax.inject.Inject

class AnimeUiMapper @Inject constructor() {

    fun map(animeEntity: AnimeEntity) =
        AnimeUiItem(
            briefInfo = "${animeEntity.name}\n${animeEntity.status}",
            kind = animeEntity.kind.name,
            logoUrl = "https://shikimori.one/" + animeEntity.images["preview"].orEmpty(),
        )

}
