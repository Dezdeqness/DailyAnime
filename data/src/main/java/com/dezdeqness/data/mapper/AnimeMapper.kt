package com.dezdeqness.data.mapper

import com.dezdeqness.data.core.TimestampConverter
import com.dezdeqness.data.fragment.HomeAnime
import com.dezdeqness.data.model.AnimeDetailsRemote
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.model.AnimeChronologyRemote
import com.dezdeqness.data.model.db.AnimeLocal
import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeChronologyEntity
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import com.dezdeqness.domain.model.HomeCalendarEntity
import com.dezdeqness.domain.model.ImageEntity
import com.dezdeqness.domain.model.StatsItemEntity
import com.dezdeqness.domain.model.UserRateEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeMapper @Inject constructor(
    private val genreMapper: GenreMapper,
    private val studioMapper: StudioMapper,
    private val videoMapper: VideoMapper,
    private val imageMapper: ImageMapper,
    private val timestampConverter: TimestampConverter,
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
            airedOnTimestamp = timestampConverter.convertToTimeStamp(item.airedOn),
            releasedOnTimestamp = timestampConverter.convertToTimeStamp(item.releasedOn)
        )

    fun fromResponse(item: HomeAnime) =
        AnimeBriefEntity(
            id = item.id.toLong(),
            name = item.name,
            russian = item.russian.orEmpty(),
            image = ImageEntity(
                preview = item.poster?.mainUrl.orEmpty(),
                original = item.poster?.originalUrl.orEmpty(),
            ),
            url = item.url,
            kind = AnimeKind.fromString(item.kind?.rawValue.orEmpty()),
            score = item.score?.toFloat() ?: 0f,
            status = AnimeStatus.fromString(item.status?.rawValue.orEmpty()),
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOnTimestamp = timestampConverter.convertToTimeStamp((item.airedOn?.date ?: "").toString()),
            releasedOnTimestamp = timestampConverter.convertToTimeStamp((item.releasedOn?.date ?: "").toString()),
            nextEpisodeTimestamp = timestampConverter.convertToTimeStamp((item.nextEpisodeAt ?: "").toString())
        )

    fun fromResponseCalendar(item: HomeAnime) =
        HomeCalendarEntity(
            id = item.id.toLong(),
            name = item.name,
            russian = item.russian.orEmpty(),
            image = ImageEntity(
                preview = item.poster?.mainUrl.orEmpty(),
                original = item.poster?.originalUrl.orEmpty(),
            ),
            url = item.url,
            kind = AnimeKind.fromString(item.kind?.rawValue.orEmpty()),
            score = item.score?.toFloat() ?: 0f,
            status = AnimeStatus.fromString(item.status?.rawValue.orEmpty()),
            episodes = item.episodes,
            episodesAired = item.episodesAired,
            airedOnTimestamp = timestampConverter.convertToTimeStamp((item.airedOn?.date ?: "").toString()),
            releasedOnTimestamp = timestampConverter.convertToTimeStamp((item.releasedOn?.date ?: "").toString()),
            nextEpisodeTimestamp = timestampConverter.convertToTimeStamp((item.nextEpisodeAt ?: "").toString()),
            description = item.descriptionHtml,
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
            airedOnTimestamp = timestampConverter.convertToTimeStamp(item.airedOn),
            releasedOnTimestamp = timestampConverter.convertToTimeStamp(item.releasedOn),
            episodes = item.episodes,
            url = item.url,
            status = AnimeStatus.fromString(item.status),
            episodesAired = item.episodesAired,
            image = imageMapper.fromResponse(item.image),
            descriptionHTML = item.descriptionHTML,
            description = item.description,
            studioList = item.studios.map(studioMapper::fromResponse),
            genreList = item.genres.map(genreMapper::fromResponse),
            videoList = item.videos.map(videoMapper::fromResponse),
            nextEpisodeAtTimestamp = timestampConverter.convertToTimeStamp(item.nextEpisodeAt),
            userRate = item.userRate?.let { userRate ->
                UserRateEntity(
                    id = userRate.id,
                    score = userRate.score,
                    status = userRate.status,
                    text = userRate.text.orEmpty(),
                    episodes = userRate.episodes ?: 0,
                    chapters = userRate.chapters ?: 0,
                    volumes = userRate.volumes ?: 0,
                    textHTML = userRate.textHTML,
                    rewatches = userRate.rewatches,
                    createdAtTimestamp = timestampConverter.convertToTimeStampWithTime(userRate.createdAt),
                    updatedAtTimestamp = timestampConverter.convertToTimeStampWithTime(userRate.updatedAt),
                )
            },
            scoresStats = item.ratesScoresStats?.map { score ->
                StatsItemEntity(
                    name = score.name,
                    value = score.value,
                )
            } ?: listOf(),
            statusesStats = item.ratesStatusesStats?.map { status ->
                StatsItemEntity(
                    name = status.name,
                    value = status.value,
                )
            } ?: listOf(),
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
            airedOnTimestamp = item.airedOnTimestamp,
            releasedOnTimestamp = item.releasedOnTimestamp,
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
            airedOnTimestamp = item.airedOnTimestamp,
            releasedOnTimestamp = item.releasedOnTimestamp,
        )

    fun fromResponse(item: AnimeChronologyRemote) =
        AnimeChronologyEntity(
            id = item.id,
            name = item.name,
            imageUrl = item.imageUrl,
            url = item.url,
            kind = item.kind.orEmpty(),
            year = item.year.orEmpty(),
        )

}
