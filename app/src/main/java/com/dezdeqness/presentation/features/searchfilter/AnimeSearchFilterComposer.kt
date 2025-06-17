package com.dezdeqness.presentation.features.searchfilter

import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.model.FilterType
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.SearchSectionUiModel
import com.dezdeqness.presentation.models.SectionType
import com.google.common.collect.ImmutableList
import javax.inject.Inject


class AnimeSearchFilterComposer @Inject constructor(
    private val resourceManager: ResourceProvider,
    private val animeSeasonCellComposer: AnimeSeasonCellComposer,
) {

    fun compose(filters: List<FilterEntity>): List<SearchSectionUiModel> {
        val animeFilters = mutableListOf<SearchSectionUiModel>()
        animeFilters.add(
            composeFilter(
                filter = filters.filter { it.type == FilterType.AUDIENCE },
                queryId = FilterType.GENRE.filterName,
                resId = FilterType.AUDIENCE.filterName,
                isExpandable = true,
                sectionType = SectionType.CheckBox,
            )
        )
        animeFilters.add(
            composeFilter(
                filter = filters.filter { it.type == FilterType.GENRE },
                resId = FilterType.GENRE.filterName,
                isExpandable = true,
                sectionType = SectionType.CheckBox,
            )
        )
        animeFilters.add(
            composeFilter(
                filter = filters.filter { it.type == FilterType.THEME },
                queryId = FilterType.GENRE.filterName,
                resId = FilterType.THEME.filterName,
                isExpandable = true,
                sectionType = SectionType.CheckBox,
            )
        )
        animeFilters.add(composeSeasonFilter())
        animeFilters.add(
            composeFilter(
                filter = filters.filter { it.type == FilterType.STATUS },
                resId = FilterType.STATUS.filterName,
            )
        )
        animeFilters.add(
            composeFilter(
                filter = filters.filter { it.type == FilterType.KIND },
                resId = FilterType.KIND.filterName,
            )
        )
        animeFilters.add(
            composeFilter(
                filter = filters.filter { it.type == FilterType.DURATION },
                resId = FilterType.DURATION.filterName,
            )
        )
        animeFilters.add(
            composeFilter(
                filter = filters.filter { it.type == FilterType.RATING },
                resId = FilterType.RATING.filterName,
            )
        )

        return animeFilters
    }

    private fun composeSeasonFilter() =
        SearchSectionUiModel(
            innerId = FilterType.SEASON.filterName,
            displayName = resourceManager.getString(PREFIX + FilterType.SEASON.filterName),
            items = animeSeasonCellComposer.composeSeasonCells(),
        )

    private fun composeFilter(
        filter: List<FilterEntity>,
        resId: String,
        queryId: String = resId,
        isExpandable: Boolean = false,
        sectionType: SectionType = SectionType.ChipMultipleChoice,
    ) =
        SearchSectionUiModel(
            innerId = resId,
            queryId = queryId,
            displayName = resourceManager.getString(PREFIX + resId),
            items = ImmutableList.copyOf(
                filter.map { item ->
                    AnimeCell(
                        id = item.id,
                        displayName = item.name
                    )
                }
            ),
            isExpandable = isExpandable,
            sectionType = sectionType,
        )

    companion object {
        private const val PREFIX = "anime_search_filter_"
    }

}
