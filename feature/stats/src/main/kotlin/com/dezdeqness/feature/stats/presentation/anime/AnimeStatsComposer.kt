package com.dezdeqness.feature.stats.presentation.anime

import androidx.compose.ui.graphics.Color
import com.dezdeqness.contract.anime.model.UserRateStatus
import com.dezdeqness.feature.stats.R
import com.dezdeqness.feature.stats.presentation.models.DiagramChartUiModel
import com.dezdeqness.feature.stats.presentation.models.ScoreChartUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsData
import com.dezdeqness.feature.stats.presentation.models.StatsHeaderUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsItem
import com.dezdeqness.foundation.provider.ResourceProvider

class AnimeStatsComposer(
    private val resourceProvider: ResourceProvider,
) {

    fun compose(arguments: AnimeStatsArguments): List<StatsItem> {
        val statsList = mutableListOf<StatsItem>()

        if (arguments.scoresArgument.isNotEmpty()) {
            val maxProgress = arguments.scoresArgument.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_scores),
                ),
            )

            val scores = arguments.scoresArgument
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
                    items = scores,
                ),
            )
        }

        if (arguments.statusesArgument.isNotEmpty()) {
            val maxProgress = arguments.statusesArgument.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_lists),
                ),
            )
            val statuses = arguments
                .statusesArgument
                .mapNotNull { value ->
                    val status = UserRateStatus.entries.find { it.name == value.name } ?: return@mapNotNull null
                    StatsData(
                        name = getStatusTitle(status),
                        value = value.value.toString(),
                        currentProgress = value.value,
                        textName = value.name,
                        color = getStatusColor(status),
                    )
                }
            statsList.add(
                DiagramChartUiModel(
                    maxProgress = maxProgress,
                    items = statuses,
                ),
            )
        }

        return statsList
    }

    private fun getStatusTitle(status: UserRateStatus) = when (status) {
        UserRateStatus.PLANNED -> R.string.stats_status_planned
        UserRateStatus.WATCHING -> R.string.stats_status_watching
        UserRateStatus.REWATCHING -> R.string.stats_status_rewatching
        UserRateStatus.DROPPED -> R.string.stats_status_dropped
        UserRateStatus.ON_HOLD -> R.string.stats_status_on_hold
        UserRateStatus.COMPLETED -> R.string.stats_status_completed
    }

    private fun getStatusColor(status: UserRateStatus) = when (status) {
        UserRateStatus.WATCHING -> Color(0xFF42A5F5)
        UserRateStatus.REWATCHING -> Color(0xFFAB47BC)
        UserRateStatus.COMPLETED -> Color(0xFF66BB6A)
        UserRateStatus.DROPPED -> Color(0xFFEF5350)
        UserRateStatus.PLANNED -> Color(0xFFFFA726)
        UserRateStatus.ON_HOLD -> Color(0xFF26C6DA)
    }
}
