package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeChronologyEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.contract.anime.model.GenreEntity
import com.dezdeqness.contract.anime.model.GenreKindEntity
import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.contract.anime.model.TypeEntity
import com.dezdeqness.contract.anime.model.UserRateEntity
import com.dezdeqness.contract.home.model.HomeCalendarEntity
import com.dezdeqness.contract.user.model.StatsItemEntity
import com.dezdeqness.data.AnimeChronologyQuery
import com.dezdeqness.data.AnimeDetailsQuery
import com.dezdeqness.data.AnimeListQuery
import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.UserRatesQuery
import com.dezdeqness.data.UserRatesSearchQuery
import com.dezdeqness.data.fragment.HomeAnime
import com.dezdeqness.data.model.AnimeBriefRemote
import com.dezdeqness.data.util.TimestampConverter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeMapper @Inject constructor(
    private val studioMapper: StudioMapper,
    private val videoMapper: VideoMapper,
    private val timestampConverter: TimestampConverter,
    private val animeBriefMapper: AnimeBriefMapper,
) {

    fun fromResponse(item: AnimeBriefRemote) = animeBriefMapper.fromResponse(item)

    fun fromResponse(item: DetailsQuery.Anime1) = AnimeBriefEntity(
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
    )

    fun fromResponse(item: HomeAnime) = AnimeBriefEntity(
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
    )

    fun fromResponseCalendar(item: HomeAnime) = HomeCalendarEntity(
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

    fun fromResponseGraphql(item: AnimeListQuery.Anime) = AnimeBriefEntity(
        id = item.id.toLong(),
        name = item.name,
        russian = item.russian.orEmpty(),
        image = ImageEntity(
            preview = item.poster?.previewUrl.orEmpty(),
            original = item.poster?.originalUrl.orEmpty(),
        ),
        url = item.url,
        kind = AnimeKind.fromString(item.kind?.rawValue.orEmpty()),
        score = item.score?.toFloat() ?: 0f,
        status = AnimeStatus.fromString(item.status?.rawValue.orEmpty()),
        episodes = item.episodes,
        episodesAired = item.episodesAired,
        airedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.airedOn?.date ?: "").toString(),
        ),
        releasedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.releasedOn?.date ?: "").toString(),
        ),
    )

    fun fromResponseGraphql(item: AnimeDetailsQuery.Anime) = AnimeDetailsEntity(
        id = item.id.toLong(),
        name = item.name,
        russian = item.russian.orEmpty(),
        english = item.english.orEmpty(),
        japanese = item.japanese.orEmpty(),
        score = item.score?.toFloat() ?: 0f,
        kind = AnimeKind.fromString(item.kind?.rawValue.orEmpty()),
        duration = item.duration?.toString().orEmpty(),
        rating = item.rating?.rawValue.orEmpty(),
        airedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.airedOn?.date ?: "").toString(),
        ),
        releasedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.releasedOn?.date ?: "").toString(),
        ),
        episodes = item.episodes,
        url = item.url,
        status = AnimeStatus.fromString(item.status?.rawValue.orEmpty()),
        episodesAired = item.episodesAired,
        image = ImageEntity(
            preview = item.poster?.previewUrl.orEmpty(),
            original = item.poster?.originalUrl.orEmpty(),
        ),
        descriptionHTML = item.descriptionHtml.orEmpty(),
        description = item.description,
        studioList = item.studios.map { studio ->
            studioMapper.fromResponseGraphql(studio)
        },
        genreList = item.genres?.map(::fromResponseGraphqlGenre).orEmpty(),
        videoList = item.videos.map { video ->
            videoMapper.fromResponseGraphql(video)
        },
        nextEpisodeAtTimestamp = timestampConverter.convertToTimeStamp(
            (item.nextEpisodeAt ?: "").toString(),
        ),
        userRate = item.userRate?.let { userRate ->
            UserRateEntity(
                id = userRate.id.toLong(),
                score = userRate.score.toLong(),
                status = userRate.status.rawValue,
                text = userRate.text.orEmpty(),
                episodes = userRate.episodes.toLong(),
                chapters = userRate.chapters.toLong(),
                volumes = userRate.volumes.toLong(),
                textHTML = "",
                rewatches = userRate.rewatches.toLong(),
                createdAtTimestamp = timestampConverter.convertToTimeStampWithTime(userRate.createdAt.toString()),
                updatedAtTimestamp = timestampConverter.convertToTimeStampWithTime(userRate.updatedAt.toString()),
            )
        },
        scoresStats = item.scoresStats?.map { score ->
            StatsItemEntity(
                name = score.score.toString(),
                value = score.count,
            )
        } ?: listOf(),
        statusesStats = item.statusesStats?.map { status ->
            StatsItemEntity(
                name = status.status.rawValue,
                value = status.count,
            )
        } ?: listOf(),
    )

    private fun fromResponseGraphqlGenre(genre: AnimeDetailsQuery.Genre) = GenreEntity(
        numericId = genre.id,
        id = "${genre.id}-${genre.name}",
        name = genre.russian,
        type = TypeEntity.fromString(genre.entryType.rawValue),
        kind = GenreKindEntity.fromString(genre.kind.rawValue),
    )

    fun fromChronologyGraphql(item: AnimeChronologyQuery.Chronology) = AnimeChronologyEntity(
        id = item.id.toLong(),
        name = item.russian.orEmpty(),
        imageUrl = item.poster?.originalUrl.orEmpty(),
        url = item.url,
        kind = item.kind?.rawValue.orEmpty(),
        year = item.airedOn?.year?.toString().orEmpty(),
    )

    fun fromResponseGraphqlUserRates(item: UserRatesSearchQuery.Anime) = AnimeBriefEntity(
        id = item.id.toLong(),
        name = item.name,
        russian = item.russian.orEmpty(),
        image = ImageEntity(
            preview = item.poster?.previewUrl.orEmpty(),
            original = item.poster?.originalUrl.orEmpty(),
        ),
        url = item.url,
        kind = AnimeKind.fromString(item.kind?.rawValue.orEmpty()),
        score = item.score?.toFloat() ?: 0f,
        status = AnimeStatus.fromString(item.status?.rawValue.orEmpty()),
        episodes = item.episodes,
        episodesAired = item.episodesAired,
        airedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.airedOn?.date ?: "").toString(),
        ),
        releasedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.releasedOn?.date ?: "").toString(),
        ),
    )

    fun fromResponseGraphqlUserRate(item: UserRatesQuery.Anime) = AnimeBriefEntity(
        id = item.id.toLong(),
        name = item.name,
        russian = item.russian.orEmpty(),
        image = ImageEntity(
            preview = item.poster?.previewUrl.orEmpty(),
            original = item.poster?.originalUrl.orEmpty(),
        ),
        url = item.url,
        kind = AnimeKind.fromString(item.kind?.rawValue.orEmpty()),
        score = item.score?.toFloat() ?: 0f,
        status = AnimeStatus.fromString(item.status?.rawValue.orEmpty()),
        episodes = item.episodes,
        episodesAired = item.episodesAired,
        airedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.airedOn?.date ?: "").toString(),
        ),
        releasedOnTimestamp = timestampConverter.convertToTimeStamp(
            (item.releasedOn?.date ?: "").toString(),
        ),
    )
}
