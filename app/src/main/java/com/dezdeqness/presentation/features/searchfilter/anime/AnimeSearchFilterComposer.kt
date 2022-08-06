package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.data.provider.ConfigurationProvider
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.FilterEntity
import com.dezdeqness.domain.model.FilterType
import com.dezdeqness.domain.model.GenreEntity
import com.dezdeqness.domain.model.TypeEntity
import com.dezdeqness.presentation.models.*
import javax.inject.Inject


class AnimeSearchFilterComposer @Inject constructor(
    private val configurationProvider: ConfigurationProvider,
    private val resourceManager: ResourceProvider,
) {

    fun compose(): List<AnimeSearchFilter> {
        val filters = configurationProvider.getFilters()
        val animeFilters = mutableListOf<AnimeSearchFilter>()
        animeFilters.add(
            composeGenreFilter(
                configurationProvider.getListGenre().filter { it.type == TypeEntity.ANIME })
        )
        animeFilters.add(composeSeasonFilter())
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.STATUS },
                ANIME_SEARCH_FILTER_STATUS
            )
        )
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.KIND },
                ANIME_SEARCH_FILTER_TYPE
            )
        )
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.DURATION },
                ANIME_SEARCH_FILTER_TIME
            )
        )
        animeFilters.add(
            composeFilter(
                filters.filter { it.type == FilterType.RATING },
                ANIME_SEARCH_FILTER_AGE_RATING
            )
        )

        return animeFilters
    }

    private fun composeGenreFilter(listGenre: List<GenreEntity>) =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_GENRE,
            displayName = resourceManager.getString(ANIME_SEARCH_FILTER_GENRE),
            items = listGenre
                .map { item -> AnimeCell(id = item.id, displayName = item.name,) }
                .sortedBy { it.displayName }
        )

    // TODO: Make time adjustment
    private fun composeSeasonFilter() =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_SEASON,
            displayName = resourceManager.getString(ANIME_SEARCH_FILTER_SEASON),
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
            displayName = resourceManager.getString(id),
            items = filter.map { item ->
                AnimeCell(
                    id = item.id,
                    displayName = item.name
                )
            }
        )

}
