package com.dezdeqness.feature.userrate.data

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnimeDbMapper @Inject constructor(
    private val imageDbMapper: ImageDbMapper,
) {

    fun toDatabase(item: AnimeBriefEntity?): AnimeLocal? {
        if (item == null) return null
        return AnimeLocal(
            id = item.id,
            name = item.name,
            russian = item.russian,
            image = imageDbMapper.toDatabase(item.image),
            url = item.url,
            kind = item.kind.kind,
            score = item.score,
            status = item.status.status,
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOnTimestamp = item.airedOnTimestamp,
            releasedOnTimestamp = item.releasedOnTimestamp,
        )
    }

    fun fromDatabase(item: AnimeLocal) = AnimeBriefEntity(
        id = item.id,
        name = item.name,
        russian = item.russian,
        image = imageDbMapper.fromDatabase(item.image),
        url = item.url,
        kind = AnimeKind.fromString(item.kind),
        score = item.score,
        status = AnimeStatus.fromString(item.status),
        episodes = item.episodes,
        episodesAired = item.episodesAired,
        airedOnTimestamp = item.airedOnTimestamp,
        releasedOnTimestamp = item.releasedOnTimestamp,
    )
}
