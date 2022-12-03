package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AnimeDetailsRemote
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.model.db.AnimeLocal
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeMapper @Inject constructor(
    private val genreMapper: GenreMapper,
    private val studioMapper: StudioMapper,
    private val videoMapper: VideoMapper,
    private val imageMapper: ImageMapper,
) {

    fun fromResponse(item: AnimeBriefRemote) =
        AnimeBriefEntity(
            id = item.id,
            name = item.name,
            russian = item.russian,
            image = imageMapper.fromResponse(item.image),
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
            kind = AnimeKind.fromString(item.kind),
            duration = item.duration,
            rating = item.rating,
            airedOn = item.airedOn.orEmpty(),
            releasedOn = item.releasedOn.orEmpty(),
            episodes = item.episodes,
            url = item.url,
            status = AnimeStatus.fromString(item.status),
            episodesAired = item.episodesAired,
            image = imageMapper.fromResponse(item.image),
            description = item.descriptionHTML,
            studioList = item.studios.map(studioMapper::fromResponse),
            genreList = item.genres.map(genreMapper::fromResponse),
            videoList = item.videos.map(videoMapper::fromResponse),
            nextEpisodeAt = item.nextEpisodeAt.orEmpty(),
        )

    fun toDatabase(item: AnimeBriefEntity?): AnimeLocal? {
        if (item == null) return null
        return AnimeLocal(
            id = item.id,
            name = item.name,
            russian = item.russian,
            image = imageMapper.toDatabase(item.image),
            url = item.url,
            kind = item.kind.kind,
            score = item.score,
            status = item.status.status,
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOn = item.airedOn,
            releasedOn = item.releasedOn,
        )
    }

    fun fromDatabase(item: AnimeLocal) =
        AnimeBriefEntity(
            id = item.id,
            name = item.name,
            russian = item.russian,
            image = imageMapper.fromDatabase(item.image),
            url = item.url,
            kind = AnimeKind.fromString(item.kind),
            score = item.score,
            status = AnimeStatus.fromString(item.status),
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOn = item.airedOn.orEmpty(),
            releasedOn = item.releasedOn.orEmpty(),
        )

}
