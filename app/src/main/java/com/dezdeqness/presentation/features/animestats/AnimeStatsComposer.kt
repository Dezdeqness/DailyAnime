package com.dezdeqness.presentation.features.animestats

import androidx.compose.ui.graphics.Color
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
                        textName = value.name,
                        value = value.value.toString(),
                        currentProgress = value.value,
                        name = 0,
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
                        name = getStatusByName(value.name),
                        value = value.value.toString(),
                        currentProgress = value.value,
                        textName = value.name,
                        color = getColorByName(value.name)
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

    private fun getStatusByName(name: String) =
        when (name) {
            PLANNED -> R.string.stats_status_planned
            WATCHING -> R.string.stats_status_watching
            DROPPED -> R.string.stats_status_dropped
            ON_HOLD -> R.string.stats_status_on_hold
            else -> R.string.stats_status_completed
        }

    private fun getColorByName(name: String) =
        when (name) {
            WATCHING -> Color(0xFF42A5F5)
            COMPLETED -> Color(0xFF66BB6A)
            DROPPED -> Color(0xFFEF5350)
            PLANNED -> Color(0xFFFFA726)
            ON_HOLD -> Color(0xFF26C6DA)
            else -> Color.Gray
        }

    companion object {
        private const val PLANNED = "planned"
        private const val WATCHING = "watching"
        private const val DROPPED = "dropped"
        private const val ON_HOLD = "on_hold"
        private const val COMPLETED = "completed"
    }

}
