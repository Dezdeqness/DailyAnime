package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.presentation.models.*


class AnimeSearchFilterComposer {

    fun compose(): List<AnimeSearchFilter> {
        val animeFilters = mutableListOf<AnimeSearchFilter>()
        animeFilters.add(composeGenreFilter())
        animeFilters.add(composeSeasonFilter())
        animeFilters.add(composeStatusFilter())
        animeFilters.add(composeTypeFilter())
        animeFilters.add(composeTimeFilter())
        animeFilters.add(composeAgeRatingFilter())

        return animeFilters
    }

    private fun composeGenreFilter() =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_GENRE,
            displayName = "Жанр",
            items = listOf()
        )

    private fun composeSeasonFilter() =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_SEASON,
            displayName = "Сезон",
            items = listOf(
                AnimeCell(
                    id = 0,
                    displayName = "Осень 2022"
                ),
                AnimeCell(
                    id = 1,
                    displayName = "Лето 2022"
                ),
                AnimeCell(
                    id = 2,
                    displayName = "Весна 2022"
                ),
                AnimeCell(
                    id = 3,
                    displayName = "Зима 2022"
                ),
                AnimeCell(
                    id = 4,
                    displayName = "2022 год"
                ),
                AnimeCell(
                    id = 5,
                    displayName = "2021 год"
                ),
                AnimeCell(
                    id = 6,
                    displayName = "2016-2020"
                ),
                AnimeCell(
                    id = 7,
                    displayName = "2011-2015"
                ),
                AnimeCell(
                    id = 8,
                    displayName = "2000-2010"
                ),
                AnimeCell(
                    id = 9,
                    displayName = "Более старые"
                ),
            )
        )


    private fun composeStatusFilter() =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_STATUS,
            displayName = "Статус",
            items = listOf(
                AnimeCell(
                    id = 0,
                    displayName = "Анонс"
                ),
                AnimeCell(
                    id = 1,
                    displayName = "Онгоинг"
                ),
                AnimeCell(
                    id = 2,
                    displayName = "Завершен"
                ),
                AnimeCell(
                    id = 3,
                    displayName = "Недавно вышедшее"
                ),
            )
        )

    private fun composeTypeFilter() =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_TYPE,
            displayName = "Тип",
            items = listOf(
                AnimeCell(
                    id = 0,
                    displayName = "TV сериал"
                ),
                AnimeCell(
                    id = 1,
                    displayName = "TV 13"
                ),
                AnimeCell(
                    id = 2,
                    displayName = "TV 24"
                ),
                AnimeCell(
                    id = 3,
                    displayName = "TV 48"
                ),
                AnimeCell(
                    id = 4,
                    displayName = "Спешл"
                ),
                AnimeCell(
                    id = 5,
                    displayName = "Клип"
                ),
                AnimeCell(
                    id = 6,
                    displayName = "OVA"
                ),
                AnimeCell(
                    id = 7,
                    displayName = "ONA"
                ),
            )
        )

    private fun composeTimeFilter() =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_TIME,
            displayName = "Продолжительность эпизода",
            items = listOf(
                AnimeCell(
                    id = 0,
                    displayName = "< 10 мин."
                ),
                AnimeCell(
                    id = 1,
                    displayName = "< 30 мин."
                ),
                AnimeCell(
                    id = 2,
                    displayName = "> 30 мин."
                ),
            )
        )

    private fun composeAgeRatingFilter() =
        AnimeSearchFilter(
            innerId = ANIME_SEARCH_FILTER_AGE_RATING,
            displayName = "Возрастной рейтинг",
            items = listOf(
                AnimeCell(
                    id = 0,
                    displayName = "G"
                ),
                AnimeCell(
                    id = 1,
                    displayName = "PG"
                ),
                AnimeCell(
                    id = 2,
                    displayName = "PG-13"
                ),
                AnimeCell(
                    id = 3,
                    displayName = "R-17"
                ),
                AnimeCell(
                    id = 4,
                    displayName = "R+"
                ),
            )
        )

}
