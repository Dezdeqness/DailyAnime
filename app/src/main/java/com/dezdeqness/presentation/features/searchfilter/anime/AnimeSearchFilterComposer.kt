package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.model.FilterType
import com.dezdeqness.presentation.models.*
import javax.inject.Inject


class AnimeSearchFilterComposer @Inject constructor(
    private val resourceManager: ResourceProvider,
) {

    fun compose(filters: List<FilterEntity>): List<AnimeSearchFilter> {
        val animeFilters = mutableListOf<AnimeSearchFilter>()
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.GENRE },
                FilterType.GENRE.filterName
            )
        )
        animeFilters.add(composeSeasonFilter())
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.STATUS },
                FilterType.STATUS.filterName
            )
        )
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.KIND },
                FilterType.KIND.filterName
            )
        )
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.DURATION },
                FilterType.DURATION.filterName
            )
        )
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.RATING },
                FilterType.RATING.filterName
            )
        )

        return animeFilters
    }

    // TODO: Make time adjustment
    private fun composeSeasonFilter() =
        AnimeSearchFilter(
            innerId = FilterType.SEASON.filterName,
            displayName = resourceManager.getString(PREFIX + FilterType.SEASON.filterName),
            items = listOf(
                AnimeCell(
                    id = "0",
                    displayName = "Осень 2022"
                ),
                AnimeCell(
                    id = "1",
                    displayName = "Лето 2022"
                ),
                AnimeCell(
                    id = "2",
                    displayName = "Весна 2022"
                ),
                AnimeCell(
                    id = "3",
                    displayName = "Зима 2022"
                ),
                AnimeCell(
                    id = "4",
                    displayName = "2022 год"
                ),
                AnimeCell(
                    id = "5",
                    displayName = "2021 год"
                ),
                AnimeCell(
                    id = "6",
                    displayName = "2016-2020"
                ),
                AnimeCell(
                    id = "7",
                    displayName = "2011-2015"
                ),
                AnimeCell(
                    id = "8",
                    displayName = "2000-2010"
                ),
                AnimeCell(
                    id = "9",
                    displayName = "Более старые"
                ),
            )
        )

    private fun composeFilter(filter: List<FilterEntity>, id: String) =
        AnimeSearchFilter(
            innerId = id,
            displayName = resourceManager.getString(PREFIX + id),
            items = filter.map { item ->
                AnimeCell(
                    id = item.id,
                    displayName = item.name
                )
            }
        )

    companion object {
        private const val PREFIX = "anime_search_filter_"
    }

}
