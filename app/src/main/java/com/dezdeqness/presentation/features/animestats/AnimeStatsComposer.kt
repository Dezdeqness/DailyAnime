package com.dezdeqness.presentation.features.animestats

import com.dezdeqness.R
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.DiagramChartUiModel
import com.dezdeqness.presentation.models.ScoreChartUiModel
import com.dezdeqness.presentation.models.StatsData
import com.dezdeqness.presentation.models.StatsHeaderUiModel
import com.google.common.collect.ImmutableList


class AnimeStatsComposer(
    private val resourceProvider: ResourceProvider,
) {

    fun compose(animeStatsFragment: AnimeStatsArguments): List<AdapterItem> {
        val statsList = mutableListOf<AdapterItem>()

        if (animeStatsFragment.scoresArgument.isNotEmpty()) {
            val maxProgress = animeStatsFragment.scoresArgument.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_scores)
                )
            )

            val scores = animeStatsFragment.scoresArgument
                .map { value ->
                    StatsData(
                        name = value.name,
                        value = value.value.toString(),
                        currentProgress = value.value,
                    )
                }

            statsList.add(
                ScoreChartUiModel(
                    maxProgress = maxProgress,
                    ImmutableList.copyOf(scores),
                )
            )
        }

        if (animeStatsFragment.statusesArgument.isNotEmpty()) {
            val maxProgress = animeStatsFragment.statusesArgument.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_lists)
                )
            )
            val statuses = animeStatsFragment
                .statusesArgument
                .map { value ->
                    StatsData(
                        name = value.name,
                        value = value.value.toString(),
                        currentProgress = value.value,
                    )
                }
            statsList.add(
                DiagramChartUiModel(
                    maxProgress = maxProgress,
                    ImmutableList.copyOf(statuses)
                )
            )
        }

        return statsList
    }

}
