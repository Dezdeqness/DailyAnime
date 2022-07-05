package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AnimeAPI
import com.dezdeqness.domain.model.AnimeEntity
import javax.inject.Inject

class AnimeMapper @Inject constructor() {

    fun fromResponse(item: AnimeAPI) =
        AnimeEntity(
            id = item.id,
            name = item.name,
            russian = item.russian,
            images = item.images ?: mapOf(),
            url = item.url,
            kind = item.kind,
            score = item.score,
            status = item.status,
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOn = item.airedOn.orEmpty(),
            releasedOn = item.releasedOn.orEmpty(),
        )

}
