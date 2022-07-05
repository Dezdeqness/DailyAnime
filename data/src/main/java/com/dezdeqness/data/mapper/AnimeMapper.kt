package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AnimeAPI
import com.dezdeqness.domain.model.AnimeEntity
import javax.inject.Inject

class AnimeMapper @Inject constructor(
    private val animeKindMapper: AnimeKindMapper,
    private val animeStatusMapper: AnimeStatusMapper,
) {

    fun fromResponse(item: AnimeAPI) =
        AnimeEntity(
            id = item.id,
            name = item.name,
            russian = item.russian,
            images = item.images ?: mapOf(),
            url = item.url,
            kind = animeKindMapper.map(item.kind),
            score = item.score,
            status = animeStatusMapper.map(item.status),
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOn = item.airedOn.orEmpty(),
            releasedOn = item.releasedOn.orEmpty(),
        )

}
