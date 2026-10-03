package com.dezdeqness.data.anilist.anime.mapper

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.data.anilist.graphql.fragment.AnimeBrief
import com.dezdeqness.data.anilist.graphql.type.MediaFormat
import com.dezdeqness.data.anilist.graphql.type.MediaStatus
import com.dezdeqness.data.util.TimestampConverter
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AnilistAnimeMapper @Inject constructor(
    private val timestampConverter: TimestampConverter,
) {

    fun map(item: AnimeBrief): AnimeBriefEntity {
        val status = mapStatus(item.status)
        val episodes = item.episodes ?: 0

        return AnimeBriefEntity(
            id = item.id.toLong(),
            name = mapTitle(item.title),
            russian = "",
            image = mapImage(item.coverImage),
            url = item.siteUrl.orEmpty(),
            kind = mapKind(item.format),
            score = item.averageScore?.toFloat() ?: 0f,
            status = status,
            episodes = episodes,
            episodesAired = mapEpisodesAired(item.nextAiringEpisode, status, episodes),
            airedOnTimestamp = toTimestamp(item.startDate?.year, item.startDate?.month, item.startDate?.day),
            releasedOnTimestamp = toTimestamp(item.endDate?.year, item.endDate?.month, item.endDate?.day),
            nextEpisodeTimestamp = item.nextAiringEpisode
                ?.let { TimeUnit.SECONDS.toMillis(it.airingAt.toLong()) }
                ?: 0,
        )
    }

    private fun mapTitle(title: AnimeBrief.Title?) =
        title?.let { it.userPreferred ?: it.romaji ?: it.english ?: it.native }.orEmpty()

    private fun mapImage(cover: AnimeBrief.CoverImage?) = ImageEntity(
        original = cover?.let { it.extraLarge ?: it.large }.orEmpty(),
        preview = cover?.let { it.large ?: it.medium }.orEmpty(),
        x96 = cover?.medium.orEmpty(),
    )

    private fun mapEpisodesAired(next: AnimeBrief.NextAiringEpisode?, status: AnimeStatus, episodes: Int) = when {
        next != null -> next.episode - 1
        status == AnimeStatus.RELEASED -> episodes
        else -> 0
    }

    private fun mapKind(format: MediaFormat?) = when (format) {
        MediaFormat.TV, MediaFormat.TV_SHORT -> AnimeKind.TV
        MediaFormat.MOVIE -> AnimeKind.MOVIE
        MediaFormat.SPECIAL -> AnimeKind.SPECIAL
        MediaFormat.OVA -> AnimeKind.OVA
        MediaFormat.ONA -> AnimeKind.ONA
        MediaFormat.MUSIC -> AnimeKind.MUSIC
        else -> AnimeKind.UNKNOWN
    }

    private fun mapStatus(status: MediaStatus?) = when (status) {
        MediaStatus.FINISHED -> AnimeStatus.RELEASED
        MediaStatus.RELEASING -> AnimeStatus.ONGOING
        MediaStatus.NOT_YET_RELEASED -> AnimeStatus.ANONS
        else -> AnimeStatus.UNKNOWN
    }

    private fun toTimestamp(year: Int?, month: Int?, day: Int?): Long {
        if (year == null) return 0L

        return timestampConverter.convertToTimeStamp(DATE_FORMAT.format(Locale.US, year, month ?: 1, day ?: 1))
    }

    private companion object {
        const val DATE_FORMAT = "%04d-%02d-%02d"
    }
}
