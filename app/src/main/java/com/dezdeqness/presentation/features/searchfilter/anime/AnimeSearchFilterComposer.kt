package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.model.FilterType
import com.dezdeqness.presentation.models.*
import javax.inject.Inject


class AnimeSearchFilterComposer @Inject constructor(
    private val resourceManager: ResourceProvider,
    private val animeSeasonCellComposer: AnimeSeasonCellComposer,
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

    private fun composeSeasonFilter() =
        AnimeSearchFilter(
            innerId = FilterType.SEASON.filterName,
            displayName = resourceManager.getString(PREFIX + FilterType.SEASON.filterName),
            items = animeSeasonCellComposer.composeSeasonCells(),
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
