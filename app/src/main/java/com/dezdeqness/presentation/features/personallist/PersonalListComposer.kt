package com.dezdeqness.presentation.features.personallist

import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.FullAnimeStatusesEntity
import com.dezdeqness.domain.model.PersonalListFilterEntity
import com.dezdeqness.domain.model.Sort
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.dezdeqness.presentation.models.UserRateUiModel
import com.dezdeqness.utils.AnimeKindUtils
import com.dezdeqness.utils.ImageUrlUtils
import javax.inject.Inject

class PersonalListComposer @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
    private val resourceProvider: ResourceProvider,
    private val animeKindUtils: AnimeKindUtils,
) {

    fun compose(
        filter: PersonalListFilterEntity,
        entityList: List<UserRateEntity>,
        query: String? = null,
    ): List<UserRateUiModel> {
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

        val kind = animeKindUtils.mapKind(anime.kind)

        return UserRateUiModel(
            rateId = item.id,
            id = anime.id,
            name = anime.russian,
            score = score,
            kind = kind,
            episodes = item.episodes.toInt(),
            logoUrl = imageUrlUtils.getImageWithBaseUrl(anime.image.preview),
            overallEpisodes = anime.episodes,
        )
    }

    companion object {
        private const val PREFIX_RIBBON_STATUS = "anime_personal_list_ribbon_status_"
    }

}
