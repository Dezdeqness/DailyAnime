package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.presentation.models.AnimeCell
import java.util.*
import javax.inject.Inject

class AnimeSeasonCellComposer @Inject constructor() {

    fun composeSeasonCells(): List<AnimeCell> {
        val cells = mutableListOf<AnimeCell>()

        cells += getFutureAndCurrentYear()
        cells += getPastYears()

        return cells
    }

    private fun getPastYears(): List<AnimeCell> {
        val cells = mutableListOf<AnimeCell>()

        val calendar = GregorianCalendar()
        calendar.time = Date()

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]

        val addFutureYear =
            (getCurrentSeason(month) + 1) % listSeasons.size == WINTER_SEASON || month == 11

        var currentYear = if (addFutureYear) year + 1 else year

        var counter = 0

        while (counter < 2) {
            currentYear -= counter
            cells.add(
                AnimeCell(
                    id = "$currentYear",
                    displayName = "$currentYear год",
                )
            )
            counter++
        }

        if (currentYear > MILESTONE_2016) {
            cells.add(
                AnimeCell(
                    id = "${MILESTONE_2016}_$currentYear",
                    displayName = "${MILESTONE_2016}-$currentYear",
                )
            )
            currentYear = MILESTONE_2016 - 1
        }

        if (currentYear > MILESTONE_2011) {
            cells.add(
                AnimeCell(
                    id = "${MILESTONE_2011}_$currentYear",
                    displayName = "${MILESTONE_2011}-$currentYear",
                )
            )
            currentYear = MILESTONE_2011 - 1
        }
        if (currentYear > MILESTONE_2000) {
            cells.add(
                AnimeCell(
                    id = "${MILESTONE_2000}_$currentYear",
                    displayName = "${MILESTONE_2000}-$currentYear",
                )
            )
            currentYear = MILESTONE_2000 - 1
        }
        if (currentYear > MILESTONE_1990) {
            cells.add(
                AnimeCell(
                    id = "${MILESTONE_1990}_$currentYear",
                    displayName = "${MILESTONE_1990}-$currentYear",
                )
            )
            currentYear = MILESTONE_1990 - 1
        }

        cells.add(
            AnimeCell(
                id = "${MILESTONE_1950}_$currentYear",
                displayName = "Более старые",
            )
        )

        return cells

    }

    private fun getFutureAndCurrentYear(): List<AnimeCell> {
        val cells = mutableListOf<AnimeCell>()

        val calendar = GregorianCalendar()
        calendar.time = Date()

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]

        when (getCurrentSeason(month)) {
            WINTER_SEASON -> {
                val isNewYear = month != Calendar.DECEMBER
                val cellNewYear = if (isNewYear) year else year + 1
                val cellPastYear = if (isNewYear) year - 1 else year
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SPRING_SEASON].first}_$cellNewYear",
                        displayName = "${listSeasons[SPRING_SEASON].second} $cellNewYear",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[WINTER_SEASON].first}_$cellNewYear",
                        displayName = "${listSeasons[WINTER_SEASON].second} $cellNewYear",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[FALL_SEASON].first}_$cellPastYear",
                        displayName = "${listSeasons[FALL_SEASON].second} $cellPastYear",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SUMMER_SEASON].first}_$cellPastYear",
                        displayName = "${listSeasons[SUMMER_SEASON].second} $cellPastYear",
                    )
                )
            }
            SPRING_SEASON -> {
                val cellPastYear = year - 1
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SUMMER_SEASON].first}_$year",
                        displayName = "${listSeasons[SUMMER_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SPRING_SEASON].first}_$year",
                        displayName = "${listSeasons[SPRING_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[WINTER_SEASON].first}_$year",
                        displayName = "${listSeasons[WINTER_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[FALL_SEASON].first}_$cellPastYear",
                        displayName = "${listSeasons[FALL_SEASON].second} $cellPastYear",
                    )
                )
            }
            SUMMER_SEASON -> {
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[FALL_SEASON].first}_$year",
                        displayName = "${listSeasons[FALL_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SUMMER_SEASON].first}_$year",
                        displayName = "${listSeasons[SUMMER_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SPRING_SEASON].first}_$year",
                        displayName = "${listSeasons[SPRING_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[WINTER_SEASON].first}_$year",
                        displayName = "${listSeasons[WINTER_SEASON].second} $year",
                    )
                )
            }
            else -> {
                val cellNewYear = year + 1
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[WINTER_SEASON].first}_$cellNewYear",
                        displayName = "${listSeasons[WINTER_SEASON].second} $cellNewYear",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[FALL_SEASON].first}_$year",
                        displayName = "${listSeasons[FALL_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SUMMER_SEASON].first}_$year",
                        displayName = "${listSeasons[SUMMER_SEASON].second} $year",
                    )
                )
                cells.add(
                    AnimeCell(
                        id = "${listSeasons[SPRING_SEASON].first}_$year",
                        displayName = "${listSeasons[SPRING_SEASON].second} $year",
                    )
                )
            }
        }
        return cells
    }

    private fun getCurrentSeason(month: Int): Int {
        val currentSeason = when (month) {
            11, 0, 1 -> {
                WINTER_SEASON
            }
            else -> {
                (month + 1) / 3
            }
        }
        return currentSeason
    }

    companion object {
        private val listSeasons = listOf(
            Pair(
                "winter", "Зима"
            ),
            Pair(
                "spring", "Весна"
            ),
            Pair(
                "summer", "Лето"
            ),
            Pair(
                "fall", "Осень"
            ),
        )

        private const val WINTER_SEASON = 0
        private const val SPRING_SEASON = 1
        private const val SUMMER_SEASON = 2
        private const val FALL_SEASON = 3

        private const val MILESTONE_2016 = 2016
        private const val MILESTONE_2011 = 2011
        private const val MILESTONE_2000 = 2000
        private const val MILESTONE_1990 = 1990
        private const val MILESTONE_1950 = 1950

    }

}
