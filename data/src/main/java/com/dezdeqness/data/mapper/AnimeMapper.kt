package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AnimeDetailsRemote
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.AnimeEntity
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeMapper @Inject constructor(
    private val genreMapper: GenreMapper,
    private val studioMapper: StudioMapper,
    private val videoMapper: VideoMapper,
) {

    fun fromResponse(item: AnimeBriefRemote) =
        AnimeEntity(
            id = item.id,
            name = item.name,
            russian = item.russian,
            images = item.images ?: mapOf(),
            url = item.url,
            kind = AnimeKind.fromString(item.kind),
            score = item.score,
            status = AnimeStatus.fromString(item.status),
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOn = item.airedOn.orEmpty(),
            releasedOn = item.releasedOn.orEmpty(),
        )

    fun fromResponse(item: AnimeDetailsRemote) =
        AnimeDetailsEntity(
            id = item.id,
            name = item.name,
            russian = item.russian,
            english = item.english.firstOrNull().orEmpty(),
            japanese = item.japanese.firstOrNull().orEmpty(),
            score = item.score,
            kind = item.kind,
            duration = item.duration,
            rating = item.rating,
            airedOn = item.airedOn.orEmpty(),
            releasedOn = item.releasedOn.orEmpty(),
            episodes = item.episodes,
            status = item.status,
            episodesAired = item.episodesAired,
            images = item.images ?: mapOf(),
            description = item.descriptionHTML,
            studioList = item.studios.map(studioMapper::fromResponse),
            genreList = item.genres.map(genreMapper::fromResponse),
            videoList = item.videos.map(videoMapper::fromResponse),
        )

}
