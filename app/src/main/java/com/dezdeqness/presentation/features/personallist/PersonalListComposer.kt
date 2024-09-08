package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import com.dezdeqness.domain.model.FullAnimeStatusesEntity
import com.dezdeqness.domain.model.PersonalListFilterEntity
import com.dezdeqness.domain.model.Sort
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.dezdeqness.presentation.models.UserRateUiModel
import com.dezdeqness.utils.AnimeKindUtils
import com.dezdeqness.utils.ImageUrlUtils
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class PersonalListComposer @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
    private val resourceProvider: ResourceProvider,
    private val animeKindUtils: AnimeKindUtils,
) {

    private val yearDateFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    fun compose(
        filter: PersonalListFilterEntity,
        entityList: List<UserRateEntity>,
        query: String? = null,
    ): List<AdapterItem> {
        val filteredItems = applyFilter(
            items = entityList,
            personalListFilterEntity = filter,
            query = query,
        )

        return filteredItems.mapNotNull(::convert)
    }

    fun composeStatuses(
        fullAnimeStatusesEntity: FullAnimeStatusesEntity,
    ) = fullAnimeStatusesEntity
        .list
        .filter { item -> item.size != 0L }
        .map { statusEntity ->
            RibbonStatusUiModel(
                id = statusEntity.groupedId,
                displayName = resourceProvider.getString(PREFIX_RIBBON_STATUS + statusEntity.name),
            )
        }

    fun applySelected(
        items: List<RibbonStatusUiModel>,
        currentId: String?,
    ) = items.map { it.copy(isSelected = it.id == currentId) }

    private fun applyFilter(
        items: List<UserRateEntity>,
        personalListFilterEntity: PersonalListFilterEntity,
        query: String? = null,
    ): List<UserRateEntity> {

        val list = if (query.isNullOrEmpty()) {
            items
        } else {
            items.filter {
                it.anime?.russian?.contains(query, true)
                    ?: it.anime?.name?.contains(query, true)
                    ?: false
            }
        }
            // Fix issue with different sorting from DB and API
            .sortedByDescending { it.anime?.russian }


        val sortedList = when (personalListFilterEntity.sort) {
            Sort.NAME -> {
                list.sortedBy { it.anime?.russian }
            }

            Sort.SCORE -> {
                list.sortedByDescending { it.anime?.score }
            }

            Sort.PROGRESS -> {
                list.sortedWith(compareByDescending<UserRateEntity> { it.episodes }.thenBy { it.anime?.russian })
            }

            Sort.EPISODES -> {
                list.sortedByDescending { it.anime?.episodes }
            }

            else -> {
                list
            }
        }

        return sortedList
    }

    private fun convert(item: UserRateEntity): UserRateUiModel? {

        if (item.anime == null) {
            return null
        }

        val anime = item.anime ?: return null

        val score = if (item.score == 0L) {
            "-"
        } else {
            item.score.toString()
        }

        val progress = if (item.episodes == 0L) {
            "-"
        } else {
            item.episodes.toString()
        }

        val isAnimeInProgress = item.episodes != 0L

        val stringBuilder = StringBuilder()

        if (anime.status == AnimeStatus.ONGOING || anime.status == AnimeStatus.ANONS) {
            stringBuilder.append(anime.status.status)
        }

        if (anime.airedOnTimestamp != 0L) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(SEPARATOR)
            }
            val year = yearDateFormatter.format(anime.airedOnTimestamp)
            stringBuilder.append(year)
        }

        if (anime.kind != AnimeKind.UNKNOWN) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(SEPARATOR)
            }
            stringBuilder.append(animeKindUtils.mapKind(anime.kind))
        }

        when {
            anime.kind == AnimeKind.MOVIE -> {

            }

            anime.status == AnimeStatus.ANONS && anime.episodes != 0 -> {
                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.append(SEPARATOR)
                }
                stringBuilder.append(anime.episodes)
            }

            anime.status == AnimeStatus.ONGOING -> {
                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.append(SEPARATOR)
                }
                stringBuilder.append(
                    "" + anime.episodesAired + "/" + if (anime.episodes == 0) {
                        "-"
                    } else {
                        anime.episodes.toString()
                    } + " эп."
                )
            }

            else -> {
                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.append(SEPARATOR)
                }
                stringBuilder.append(
                    "" + anime.episodes + " эп."
                )
            }
        }

        return UserRateUiModel(
            rateId = item.id,
            id = anime.id,
            name = anime.russian,
            briefInfo = stringBuilder.toString(),
            score = score,
            progress = progress,
            isAnimeInProgress = isAnimeInProgress,
            logoUrl = imageUrlUtils.getImageWithBaseUrl(anime.image.preview),
            overallEpisodes = item.episodes.toInt(),
        )
    }

    companion object {
        private const val PREFIX_RIBBON_STATUS = "anime_personal_list_ribbon_status_"
        private const val SEPARATOR = " • "
    }

}
