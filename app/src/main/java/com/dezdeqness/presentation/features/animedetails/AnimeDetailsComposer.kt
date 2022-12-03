package com.dezdeqness.presentation.features.animedetails

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
import com.dezdeqness.presentation.models.RelatedItemListUiModel
import com.dezdeqness.presentation.models.RoleUiModel
import com.dezdeqness.presentation.models.RoleUiModelList
import com.dezdeqness.presentation.models.ScreenshotUiModel
import com.dezdeqness.presentation.models.ScreenshotUiModelList
import com.dezdeqness.presentation.models.VideoUiModel
import com.dezdeqness.presentation.models.VideoUiModelList
import com.dezdeqness.utils.ImageUrlUtils
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class AnimeDetailsComposer @Inject constructor(
    private val animeUiMapper: AnimeUiMapper,
    private val imageUrlUtils: ImageUrlUtils,
) {

    fun compose(animeDetailsFullEntity: AnimeDetailsFullEntity): AnimeDetailsState {
        val details = animeDetailsFullEntity.animeDetailsEntity
        val uiItems = mutableListOf<AdapterItem>()

        if (details.genreList.isNotEmpty() || details.studioList.isNotEmpty()) {
            uiItems.add(AnimeCellList(
                list = details.studioList.map { studio ->
                    AnimeCell(
                        id = studio.id.toString(),
                        displayName = studio.name,
                    )
                }.take(1) + details.genreList.map { genre ->
                    AnimeCell(
                        id = genre.id,
                        displayName = genre.name,
                    )
                }
            ))
        }

        uiItems.add(composeBriefInfoList(details))
        uiItems.add(
            DescriptionUiModel(
                content = animeDetailsFullEntity.animeDetailsEntity.description,
            )
        )
        uiItems.add(
            RelatedItemListUiModel(
                list = animeDetailsFullEntity.relates.map(animeUiMapper::map)
            )
        )
        uiItems.add(
            RoleUiModelList(list = animeDetailsFullEntity.roles.map {
                RoleUiModel(
                    name = it.character.russian.ifEmpty {
                        it.character.name
                    },
                    imageUrl = imageUrlUtils.getImageWithBaseUrl(it.character.image.preview),
                )
            })
        )

        uiItems.add(
            ScreenshotUiModelList(
                list = animeDetailsFullEntity
                    .screenshots
                    .map { ScreenshotUiModel(imageUrlUtils.getImageWithBaseUrl(it.preview)) }
            )
        )

        uiItems.add(
            VideoUiModelList(
                list = animeDetailsFullEntity.animeDetailsEntity.videoList.map { video ->
                    VideoUiModel(
                        source = video.hosting,
                        imageUrl = imageUrlUtils.getSecurityUrl(video.imageUrl),
                        name = video.name,
                        sourceUrl = video.playerUrl,
                    )
                }
            )
        )

        return AnimeDetailsState(
            title = details.russian,
            imageUrl = imageUrlUtils.getImageWithBaseUrl(details.image.original),
            ratingScore = details.score,
            uiModels = uiItems,
        )
    }

    private fun composeBriefInfoList(details: AnimeDetailsEntity): AdapterItem {
        val list = mutableListOf<BriefInfoUiModel>()

        if (details.status == AnimeStatus.ANONS || details.status == AnimeStatus.ONGOING) {
            list.add(
                BriefInfoUiModel(
                    info = details.status.status,
                    title = "Статус"
                )
            )
        }

        if (details.status == AnimeStatus.RELEASED || details.status == AnimeStatus.LATEST) {
            list.add(
                BriefInfoUiModel(
                    info = details.airedOn + "\n" + details.releasedOn,
                    title = "Дата выхода"
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
                        info = "Несколько мгновений",
                        title = "До ${details.episodesAired + 1} эпизода"
                    )
                )
            }

        }

        list.add(
            BriefInfoUiModel(
                info = details.kind.kind,
                title = "Тип"
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
                    title = "Эпизоды"
                )
            )

            list.add(
                BriefInfoUiModel(
                    info = "${details.duration} мин",
                    title = "Время эпизода"
                )
            )
        }

        if (details.rating.isNotEmpty()) {
            list.add(
                BriefInfoUiModel(
                    info = details.rating,
                    title = "Возрастной рейтинг"
                )
            )
        }

        return BriefInfoUiModelList(list)
    }

}
