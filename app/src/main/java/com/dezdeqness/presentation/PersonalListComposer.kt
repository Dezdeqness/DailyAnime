package com.dezdeqness.presentation

import com.dezdeqness.domain.model.AnimeKind
import com.dezdeqness.domain.model.AnimeStatus
import com.dezdeqness.domain.model.FullAnimeStatusesEntity
import com.dezdeqness.domain.model.PersonalListFilterEntity
import com.dezdeqness.domain.model.Sort
import com.dezdeqness.domain.model.UserRateEntity
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.PersonalListFilterUiModel
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.dezdeqness.presentation.models.UserRateUiModel
import com.dezdeqness.utils.ImageUrlUtils
import javax.inject.Inject

class PersonalListComposer @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
) {

    fun composeStatuses(
        fullAnimeStatusesEntity: FullAnimeStatusesEntity,
        currentId: String?,
    ): List<RibbonStatusUiModel> {

        return fullAnimeStatusesEntity
            .list
            .filter { item -> item.size != 0L }
            .mapIndexed { index, statusEntity ->

                val isSelected = (currentId.isNullOrEmpty() && index == 0)
                        || statusEntity.groupedId == currentId

                RibbonStatusUiModel(
                    id = statusEntity.groupedId,
                    displayName = statusEntity.name.replace("_", "").toUpperCase(),
                    isSelected = isSelected,
                )
            }
    }

    fun addFilter(
        items: List<AdapterItem>,
        personalListFilterEntity: PersonalListFilterEntity,
    ): List<AdapterItem> {
        val mutableList = mutableListOf<AdapterItem>()
        mutableList.addAll(items)
        mutableList.add(
            0,
            PersonalListFilterUiModel(
                sort = personalListFilterEntity.sort,
                isAscending = personalListFilterEntity.isAscending,
            ),
        )
        return mutableList
    }

    fun applyFilter(
        items: List<UserRateEntity>,
        personalListFilterEntity: PersonalListFilterEntity,
        query: String? = null,
    ): List<UserRateEntity> {

        val list = if (query.isNullOrEmpty()) {
            items
        } else {
            items.filter {
                it.anime?.russian?.contains(query,true)
                    ?: it.anime?.name?.contains(query, true)
                    ?: false
            }
        }

        val sort = personalListFilterEntity.sort
        val isAscending = personalListFilterEntity.isAscending
        when (sort) {
            Sort.NAME -> {
                return if (isAscending) {
                    list.sortedBy {
                        it.anime?.russian
                    }
                } else {
                    list.sortedByDescending {
                        it.anime?.russian
                    }
                }
            }
            Sort.SCORE -> {
                return if (isAscending) {
                    list.sortedBy {
                        it.score
                    }
                } else {
                    list.sortedByDescending {
                        it.score
                    }
                }
            }
            Sort.PROGRESS -> {
                return if (isAscending) {
                    list.sortedBy {
                        it.episodes
                    }
                } else {
                    list.sortedByDescending {
                        it.episodes
                    }
                }
            }
            Sort.EPISODES -> {
                return if (isAscending) {
                    list.sortedBy {
                        it.anime?.episodes
                    }
                } else {
                    list.sortedByDescending {
                        it.anime?.episodes

                    }
                }
            }
            else -> {
                return list
            }
        }
    }

    fun convert(item: UserRateEntity): UserRateUiModel? {

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

        val stringBuilder = StringBuilder()

        if (anime.status == AnimeStatus.ONGOING || anime.status == AnimeStatus.ANONS) {
            stringBuilder.append(anime.status.status)
        }

        if (anime.airedOn.isNotEmpty()) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(SEPARATOR)
            }
            stringBuilder.append(anime.airedOn)
        }

        if (anime.kind != AnimeKind.UNKNOWN) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(SEPARATOR)
            }
            stringBuilder.append(anime.kind.kind)
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
            logoUrl = imageUrlUtils.getImageWithBaseUrl(anime.image.preview),
        )
    }

    companion object {
        private const val SEPARATOR = " • "
    }

}
