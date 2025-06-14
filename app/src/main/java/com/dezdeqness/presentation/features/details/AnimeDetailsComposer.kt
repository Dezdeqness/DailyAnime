package com.dezdeqness.presentation.features.details

import com.dezdeqness.R
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import com.dezdeqness.domain.model.CharacterDetailsEntity
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeCellList
import com.dezdeqness.presentation.models.AnimeItemListUiModel
import com.dezdeqness.presentation.models.BriefInfoUiModel
import com.dezdeqness.presentation.models.BriefInfoUiModelList
import com.dezdeqness.presentation.models.DescriptionUiModel
import com.dezdeqness.presentation.models.HeaderItemUiModel
import com.dezdeqness.presentation.models.MoreInfoUiModel
import com.dezdeqness.presentation.models.NameUiModel
import com.dezdeqness.presentation.models.RelatedItemListUiModel
import com.dezdeqness.presentation.models.RoleUiModel
import com.dezdeqness.presentation.models.RoleUiModelList
import com.dezdeqness.presentation.models.ScreenshotUiModelList
import com.dezdeqness.presentation.models.SeyuModelList
import com.dezdeqness.presentation.models.SpacerUiItem
import com.dezdeqness.presentation.models.VideoUiModel
import com.dezdeqness.presentation.models.VideoUiModelList
import com.dezdeqness.utils.AnimeKindUtils
import com.dezdeqness.utils.ImageUrlUtils
import com.google.common.collect.ImmutableList
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import javax.inject.Inject
import kotlin.text.ifEmpty

class AnimeDetailsComposer @Inject constructor(
    private val animeUiMapper: AnimeUiMapper,
    private val imageUrlUtils: ImageUrlUtils,
    private val resourceProvider: ResourceProvider,
    private val animeKindUtils: AnimeKindUtils,
) {

    private val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    private val fullDateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val shortDateFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    fun compose(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
    ): ImmutableList<AdapterItem> {
        val details = animeDetailsFullEntity.animeDetailsEntity
        val uiItems = mutableListOf<AdapterItem>()

        uiItems.add(
            HeaderItemUiModel(
                imageUrl = imageUrlUtils.getImageWithBaseUrl(details.image.original),
                ratingScore = details.score,
            )
        )

        uiItems.add(NameUiModel(title = details.russian))

        uiItems.add(composeBriefInfoList(details))

        if (details.genreList.isNotEmpty() || details.studioList.isNotEmpty()) {
            uiItems.add(
                AnimeCellList(
                    list = ImmutableList.copyOf(
                        details
                        .genreList
                        .map { genre ->
                            AnimeCell(
                                id = genre.id,
                                displayName = genre.name,
                            )
                        }
                        .sortedBy { it.displayName })
                )
            )
        }

        if (details.description != null) {
            uiItems.add(DescriptionUiModel(content = details.descriptionHTML))
        }

        uiItems.add(MoreInfoUiModel)

        addRelatesIfNotEmpty(
            animeDetailsFullEntity = animeDetailsFullEntity,
            uiItems = uiItems,
        )

        addRolesIfNotEmpty(
            animeDetailsFullEntity = animeDetailsFullEntity,
            uiItems = uiItems,
        )

        addScreenshotsIfNotEmpty(
            animeDetailsFullEntity = animeDetailsFullEntity,
            uiItems = uiItems,
        )

        addVideosIfNotEmpty(
            animeDetailsFullEntity = animeDetailsFullEntity,
            uiItems = uiItems,
        )

        uiItems.add(SpacerUiItem)

        return ImmutableList.copyOf(uiItems)
    }

    private fun addRelatesIfNotEmpty(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
        uiItems: MutableList<AdapterItem>,
    ) =
        animeDetailsFullEntity
            .relates
            .map(animeUiMapper::map)
            .takeIf { it.isNotEmpty() }
            ?.let { list ->
                uiItems.add(RelatedItemListUiModel(list = ImmutableList.copyOf(list)))
            }

    private fun addVideosIfNotEmpty(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
        uiItems: MutableList<AdapterItem>,
    ) =
        animeDetailsFullEntity
            .animeDetailsEntity
            .videoList
            .filterNot { it.hosting == EXCLUDED_HOSTING }
            .map { video ->
                VideoUiModel(
                    source = video.hosting,
                    imageUrl = imageUrlUtils.getSecurityUrl(video.imageUrl),
                    name = video.name,
                    sourceUrl = video.playerUrl,
                )
            }.takeIf { it.isNotEmpty() }
            ?.let { list ->
                uiItems.add(VideoUiModelList(list = ImmutableList.copyOf(list)))
            }

    private fun addRolesIfNotEmpty(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
        uiItems: MutableList<AdapterItem>,
    ) = animeDetailsFullEntity
        .roles
        .map {
            RoleUiModel(
                id = it.character.id,
                name = it.character.russian.ifEmpty { it.character.name },
                imageUrl = it.character.image.preview,
            )
        }
        .takeIf { it.isNotEmpty() }
        ?.let { list ->
            uiItems.add(RoleUiModelList(list = ImmutableList.copyOf(list)))
        }

    private fun addScreenshotsIfNotEmpty(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
        uiItems: MutableList<AdapterItem>,
    ) = animeDetailsFullEntity
        .screenshots
        .map { it.preview }
        .takeIf { it.isNotEmpty() }
        ?.let { list ->
            uiItems.add(ScreenshotUiModelList(list = ImmutableList.copyOf(list)))
        }


    private fun composeBriefInfoList(details: AnimeDetailsEntity): AdapterItem {
        val list = mutableListOf<BriefInfoUiModel>()

        if (details.status == AnimeStatus.ANONS || details.status == AnimeStatus.ONGOING) {
            list.add(
                BriefInfoUiModel(
                    info = resourceProvider.getString(PREFIX_STATUS + details.status.status),
                    title = resourceProvider.getString(R.string.anime_details_status)
                )
            )
        }

        if (details.status == AnimeStatus.RELEASED || details.status == AnimeStatus.LATEST) {
            val releasedYear = GregorianCalendar().apply {
                time = Date(details.releasedOnTimestamp)
            }.get(Calendar.YEAR)

            val airedYear = GregorianCalendar().apply {
                time = Date(details.airedOnTimestamp)
            }.get(Calendar.YEAR)

            val isSameYear = releasedYear == airedYear

            val stringBuilder = StringBuilder()

            var releasedDate = ""
            if (details.releasedOnTimestamp != 0L) {
                releasedDate = if (isSameYear) {
                    dateFormatter.format(details.releasedOnTimestamp)
                } else {
                    fullDateFormatter.format(details.releasedOnTimestamp)
                }
            }

            val airedDate = if (details.kind != AnimeKind.TV && details.episodes < 2) {
                shortDateFormatter.format(details.airedOnTimestamp)
            } else {
                if (isSameYear) {
                    dateFormatter.format(details.airedOnTimestamp)
                } else {
                    fullDateFormatter.format(details.airedOnTimestamp)
                }
            }

            stringBuilder.append(airedDate)

            if (releasedDate.isNotEmpty()) {
                if (isSameYear) {
                    stringBuilder.append(" - ")
                } else {
                    stringBuilder.append("\n")
                }
                stringBuilder.append(releasedDate)
                if (isSameYear) {
                    stringBuilder.append(" ")
                    stringBuilder.append(releasedYear)
                }
            } else {
                if (isSameYear) {
                    stringBuilder.append(" ")
                    stringBuilder.append(airedYear)
                }
            }

            list.add(
                BriefInfoUiModel(
                    info = stringBuilder.toString(),
                    title = resourceProvider.getString(R.string.anime_details_date),
                )
            )
        } else if (details.status == AnimeStatus.ONGOING) {
            val date = Date(details.nextEpisodeAtTimestamp)

            val currentDate = Date()

            if (currentDate.time - date.time < 0) {
                list.add(
                    BriefInfoUiModel(
                        info = resourceProvider.getString(R.string.anime_details_episode_date),
                        title = resourceProvider.getString(
                            R.string.anime_details_date_next_episode,
                            details.episodesAired + 1
                        ),
                    )
                )
            }

        }

        list.add(
            BriefInfoUiModel(
                info = animeKindUtils.mapKind(details.kind),
                title = resourceProvider.getString(R.string.anime_details_type_title),
            )
        )

        if (details.status != AnimeStatus.ANONS) {
            val episodes = if (details.status == AnimeStatus.RELEASED) {
                details.episodes.toString()
            } else {
                "${details.episodesAired}/${details.episodes.takeIf { it != 0 } ?: "-"}"
            }
            list.add(
                BriefInfoUiModel(
                    info = episodes,
                    title = resourceProvider.getString(R.string.anime_details_episodes_title),
                )
            )

            list.add(
                BriefInfoUiModel(
                    info = resourceProvider.getString(
                        R.string.anime_details_episode_time,
                        details.duration,
                    ),
                    title = resourceProvider.getString(R.string.anime_details_episode_time_title),
                )
            )
        }

        if (details.rating.isNotEmpty() && details.rating != "none") {
            list.add(
                BriefInfoUiModel(
                    info = resourceProvider.getString(PREFIX_AGE_RATING + details.rating),
                    title = resourceProvider.getString(R.string.anime_details_age_rating_title),
                )
            )
        }

        return BriefInfoUiModelList(ImmutableList.copyOf(list))
    }

    fun compose(entity: CharacterDetailsEntity): ImmutableList<AdapterItem> {
        val uiItems = mutableListOf<AdapterItem>()

        uiItems.add(
            HeaderItemUiModel(
                imageUrl = imageUrlUtils.getImageWithBaseUrl(entity.image.original),
            )
        )

        uiItems.add(NameUiModel(title = entity.russian))

        if (entity.description != null) {
            uiItems.add(DescriptionUiModel(content = entity.descriptionHTML))
        }
        entity
            .seyuList
            .map {
                RoleUiModel(
                    id = it.id,
                    name = it.russian.ifEmpty { it.name },
                    imageUrl = imageUrlUtils.getImageWithBaseUrl(it.image.preview),
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let { list ->
                uiItems.add(SeyuModelList(list = ImmutableList.copyOf(list)))
            }

        animeUiMapper.map(entity.animeList)
            .takeIf { it.isNotEmpty() }
            ?.let { list ->
                uiItems.add(AnimeItemListUiModel(list = ImmutableList.copyOf(list)))
            }

        uiItems.add(SpacerUiItem)

        return ImmutableList.copyOf(uiItems)
    }

    companion object {
        private const val EXCLUDED_HOSTING = "vk"
        private const val PREFIX_STATUS = "anime_status_"
        private const val PREFIX_AGE_RATING = "anime_details_age_rating_"
    }

}
