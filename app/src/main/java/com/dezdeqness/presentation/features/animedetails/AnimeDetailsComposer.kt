package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.R
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.AnimeDetailsEntity
import com.dezdeqness.domain.model.AnimeDetailsFullEntity
import com.dezdeqness.domain.model.AnimeStatus
import com.dezdeqness.presentation.AnimeUiMapper
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeCellList
import com.dezdeqness.presentation.models.BriefInfoUiModel
import com.dezdeqness.presentation.models.BriefInfoUiModelList
import com.dezdeqness.presentation.models.DescriptionUiModel
import com.dezdeqness.presentation.models.HeaderItemUiModel
import com.dezdeqness.presentation.models.MoreInfoUiModel
import com.dezdeqness.presentation.models.RelatedItemListUiModel
import com.dezdeqness.presentation.models.RoleUiModel
import com.dezdeqness.presentation.models.RoleUiModelList
import com.dezdeqness.presentation.models.ScreenshotUiModel
import com.dezdeqness.presentation.models.ScreenshotUiModelList
import com.dezdeqness.presentation.models.SpacerUiItem
import com.dezdeqness.presentation.models.VideoUiModel
import com.dezdeqness.presentation.models.VideoUiModelList
import com.dezdeqness.utils.ImageUrlUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AnimeDetailsComposer @Inject constructor(
    private val animeUiMapper: AnimeUiMapper,
    private val imageUrlUtils: ImageUrlUtils,
    private val resourceProvider: ResourceProvider,
) {

    fun compose(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
    ): List<AdapterItem> {
        val details = animeDetailsFullEntity.animeDetailsEntity
        val uiItems = mutableListOf<AdapterItem>()

        uiItems.add(
            HeaderItemUiModel(
                title = details.russian,
                imageUrl = imageUrlUtils.getImageWithBaseUrl(details.image.original),
                ratingScore = details.score,
            )
        )

        uiItems.add(composeBriefInfoList(details))

        uiItems.add(MoreInfoUiModel)

        if (details.description != null) {
            uiItems.add(DescriptionUiModel(content = details.descriptionHTML))
        }

        if (details.genreList.isNotEmpty() || details.studioList.isNotEmpty()) {
            uiItems.add(AnimeCellList(
                list = details
                    .genreList
                    .map { genre ->
                        AnimeCell(
                            id = genre.id,
                            displayName = genre.name,
                        )
                    }
                    .sortedBy { it.displayName }
            ))
        }

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

        return uiItems
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
                uiItems.add(
                    RelatedItemListUiModel(
                        list = list
                    )
                )
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
                uiItems.add(
                    VideoUiModelList(
                        list = list
                    )
                )
            }

    private fun addRolesIfNotEmpty(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
        uiItems: MutableList<AdapterItem>,
    ) = animeDetailsFullEntity
        .roles
        .map {
            RoleUiModel(
                name = it.character.russian.ifEmpty {
                    it.character.name
                },
                imageUrl = imageUrlUtils.getImageWithBaseUrl(it.character.image.preview),
            )
        }
        .takeIf { it.isNotEmpty() }
        ?.let { list ->
            uiItems.add(
                RoleUiModelList(
                    list = list
                )
            )
        }

    private fun addScreenshotsIfNotEmpty(
        animeDetailsFullEntity: AnimeDetailsFullEntity,
        uiItems: MutableList<AdapterItem>,
    ) = animeDetailsFullEntity
        .screenshots
        .map { ScreenshotUiModel(imageUrlUtils.getImageWithBaseUrl(it.preview)) }
        .takeIf { it.isNotEmpty() }
        ?.let { list ->
            uiItems.add(
                ScreenshotUiModelList(
                    list = list
                )
            )
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
            list.add(
                BriefInfoUiModel(
                    info = details.airedOn + "\n" + details.releasedOn,
                    title = resourceProvider.getString(R.string.anime_details_date),
                )
            )
        } else if (details.status == AnimeStatus.ONGOING) {

            val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
            val simpleDateFormat = SimpleDateFormat(pattern)

            var date: Date? = null
            try {
                date = simpleDateFormat.parse(details.nextEpisodeAt)
            } catch (_: Exception) {

            }

            val currentDate = Date()

            if (currentDate.time - (date?.time ?: 0) < 0) {
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
                info = details.kind.kind.uppercase(Locale.getDefault()),
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

        if (details.rating.isNotEmpty()) {
            list.add(
                BriefInfoUiModel(
                    info = resourceProvider.getString(PREFIX_AGE_RATING + details.rating),
                    title = resourceProvider.getString(R.string.anime_details_age_rating_title),
                )
            )
        }

        return BriefInfoUiModelList(list)
    }

    companion object {
        private const val EXCLUDED_HOSTING = "vk"
        private const val PREFIX_STATUS = "anime_status_"
        private const val PREFIX_AGE_RATING = "anime_details_age_rating_"
    }

}
