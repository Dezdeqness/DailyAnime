package com.dezdeqness.feature.details.anime.presentation.composer

import com.dezdeqness.contract.anime.model.AnimeDetailsEntity
import com.dezdeqness.contract.anime.model.AnimeDetailsFullEntity
import com.dezdeqness.contract.anime.model.AnimeKind
import com.dezdeqness.contract.anime.model.AnimeStatus
import com.dezdeqness.contract.source.SourceAnimeStats
import com.dezdeqness.contract.source.SourceCharacterDetails
import com.dezdeqness.contract.source.SourceChronology
import com.dezdeqness.contract.source.SourceConfig
import com.dezdeqness.contract.source.SourceSimilar
import com.dezdeqness.contract.source.hasFeature
import com.dezdeqness.feature.details.anime.R
import com.dezdeqness.feature.details.anime.presentation.models.AnimeDetailsSection
import com.dezdeqness.feature.details.anime.presentation.models.CharacterItem
import com.dezdeqness.feature.details.anime.presentation.models.GenreChip
import com.dezdeqness.feature.details.anime.presentation.models.RelatedItem
import com.dezdeqness.feature.details.anime.presentation.models.ScreenshotItem
import com.dezdeqness.feature.details.anime.presentation.models.VideoItem
import com.dezdeqness.feature.details.common.presentation.DetailsSection
import com.dezdeqness.feature.details.common.presentation.sections.BottomSpacerSection
import com.dezdeqness.feature.details.common.presentation.sections.BriefInfoSection
import com.dezdeqness.feature.details.common.presentation.sections.DescriptionSection
import com.dezdeqness.feature.details.common.presentation.sections.HeaderSection
import com.dezdeqness.feature.details.common.presentation.sections.TitleSection
import com.dezdeqness.foundation.provider.ResourceProvider
import com.dezdeqness.foundation.ui.views.details.BriefInfoEntry
import com.dezdeqness.shared.presentation.mapper.AnimeLabelMapper
import com.dezdeqness.shared.presentation.utils.AnimeKindUtils
import com.dezdeqness.shared.presentation.utils.UrlNormalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import javax.inject.Inject

class AnimeDetailsComposer @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val animeKindUtils: AnimeKindUtils,
    private val urlNormalizer: UrlNormalizer,
    private val sourceConfig: SourceConfig,
    private val animeLabelMapper: AnimeLabelMapper,
) {

    private val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    private val fullDateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val shortDateFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    fun compose(full: AnimeDetailsFullEntity): List<DetailsSection> {
        val details = full.animeDetailsEntity
        val sections = mutableListOf<DetailsSection>()

        sections += HeaderSection(
            imageUrl = urlNormalizer.normalize(details.image.original),
            rating = details.score,
        )

        sections += TitleSection(text = details.russian.ifEmpty { details.name })

        sections += BriefInfoSection(items = composeBriefInfo(details))

        if (details.genreList.isNotEmpty()) {
            sections += AnimeDetailsSection.Genres(
                items = details.genreList
                    .map { GenreChip(id = it.id, name = it.name) }
                    .sortedBy { it.name },
            )
        }

        if (details.description != null) {
            sections += DescriptionSection(html = details.descriptionHTML)
        }

        val moreInfo = AnimeDetailsSection.MoreInfo(
            isSimilarVisible = sourceConfig.hasFeature<SourceSimilar>(),
            isChronologyVisible = sourceConfig.hasFeature<SourceChronology>(),
            isStatsVisible = sourceConfig.hasFeature<SourceAnimeStats>(),
        )
        if (moreInfo.isSimilarVisible || moreInfo.isChronologyVisible || moreInfo.isStatsVisible) {
            sections += moreInfo
        }

        full.relates
            .map {
                val brief = it.animeBriefEntity
                RelatedItem(
                    id = brief.id,
                    imageUrl = urlNormalizer.normalize(brief.image.original),
                    type = animeLabelMapper.relationTitle(it.kind),
                    briefInfo = listOfNotNull(
                        animeKindUtils.mapKind(brief.kind),
                        brief.airedOnTimestamp.takeIf { it != 0L }?.let(yearFormatter::format),
                    ).joinToString(" • "),
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let { sections += AnimeDetailsSection.Related(items = it) }

        full.roles
            .map {
                CharacterItem(
                    id = it.character.id,
                    name = it.character.russian.ifEmpty { it.character.name },
                    imageUrl = urlNormalizer.normalize(it.character.image.preview),
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let {
                sections += AnimeDetailsSection.Characters(
                    items = it,
                    isClickable = sourceConfig.hasFeature<SourceCharacterDetails>(),
                )
            }

        full.screenshots
            .map {
                ScreenshotItem(
                    previewUrl = urlNormalizer.normalize(it.preview),
                    originalUrl = urlNormalizer.normalize(it.original),
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let { sections += AnimeDetailsSection.Screenshots(items = it) }

        details.videoList
            .filterNot { it.hosting == EXCLUDED_HOSTING }
            .map {
                VideoItem(
                    imageUrl = secureUrl(it.imageUrl),
                    name = animeLabelMapper.videoTitle(it),
                    sourceUrl = it.url,
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let { sections += AnimeDetailsSection.Videos(items = it) }

        sections += BottomSpacerSection

        return sections
    }

    private fun composeBriefInfo(details: AnimeDetailsEntity): List<BriefInfoEntry> {
        val list = mutableListOf<BriefInfoEntry>()

        if (details.status == AnimeStatus.ANONS || details.status == AnimeStatus.ONGOING) {
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.anime_details_brief_status),
                info = resourceProvider.getString(PREFIX_STATUS + details.status.status),
            )
        }

        if (details.status == AnimeStatus.RELEASED || details.status == AnimeStatus.LATEST) {
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.anime_details_brief_date),
                info = formatAiredReleased(details),
            )
        } else if (details.status == AnimeStatus.ONGOING) {
            val nextDate = Date(details.nextEpisodeAtTimestamp)
            if (Date().time - nextDate.time < 0) {
                list += BriefInfoEntry(
                    title = resourceProvider.getString(
                        R.string.anime_details_brief_next_episode_title,
                        details.episodesAired + 1,
                    ),
                    info = resourceProvider.getString(R.string.anime_details_brief_next_episode_value),
                )
            }
        }

        list += BriefInfoEntry(
            title = resourceProvider.getString(R.string.anime_details_brief_type),
            info = animeKindUtils.mapKind(details.kind),
        )

        if (details.status != AnimeStatus.ANONS) {
            val episodes = if (details.status == AnimeStatus.RELEASED) {
                details.episodes.toString()
            } else {
                "${details.episodesAired}/${details.episodes.takeIf { it != 0 } ?: "-"}"
            }
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.anime_details_brief_episodes),
                info = episodes,
            )
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.anime_details_brief_episode_time),
                info = resourceProvider.getString(
                    R.string.anime_details_brief_episode_time_value,
                    details.duration,
                ),
            )
        }

        animeLabelMapper.ageRatingTitle(details.rating)?.let { rating ->
            list += BriefInfoEntry(
                title = resourceProvider.getString(R.string.anime_details_brief_age_rating),
                info = rating,
            )
        }

        return list
    }

    private fun formatAiredReleased(details: AnimeDetailsEntity): String {
        val releasedYear = GregorianCalendar()
            .apply { time = Date(details.releasedOnTimestamp) }
            .get(Calendar.YEAR)
        val airedYear = GregorianCalendar()
            .apply { time = Date(details.airedOnTimestamp) }
            .get(Calendar.YEAR)
        val isSameYear = releasedYear == airedYear

        val released = if (details.releasedOnTimestamp != 0L) {
            if (isSameYear) {
                dateFormatter.format(details.releasedOnTimestamp)
            } else {
                fullDateFormatter.format(details.releasedOnTimestamp)
            }
        } else {
            ""
        }

        val aired = if (details.kind != AnimeKind.TV && details.episodes < 2) {
            shortDateFormatter.format(details.airedOnTimestamp)
        } else if (isSameYear) {
            dateFormatter.format(details.airedOnTimestamp)
        } else {
            fullDateFormatter.format(details.airedOnTimestamp)
        }

        return buildString {
            append(aired)
            if (released.isNotEmpty()) {
                append(if (isSameYear) " - " else "\n")
                append(released)
                if (isSameYear) append(" ").append(releasedYear)
            } else if (isSameYear) {
                append(" ").append(airedYear)
            }
        }
    }

    private fun secureUrl(url: String): String =
        if (url.startsWith("http://")) url.replaceFirst("http://", "https://") else url

    companion object {
        private const val EXCLUDED_HOSTING = "vk"
        private const val PREFIX_STATUS = "anime_status_"
    }
}
