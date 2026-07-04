package com.dezdeqness.feature.stats.presentation.profile

import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.feature.stats.R
import com.dezdeqness.feature.stats.presentation.models.ScoreChartUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsChartUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsData
import com.dezdeqness.feature.stats.presentation.models.StatsHeaderUiModel
import com.dezdeqness.feature.stats.presentation.models.StatsItem
import com.dezdeqness.foundation.provider.ResourceProvider

class ProfileStatsComposer(
    private val resourceProvider: ResourceProvider,
) {

    fun compose(account: AccountEntity): List<StatsItem> {
        val statsList = mutableListOf<StatsItem>()

        if (account.types.isNotEmpty()) {
            val maxProgress = account.types.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_types),
                ),
            )

            val scores = account
                .types
                .map { value ->
                    StatsData(
                        textName = value.name,
                        value = value.value.toString(),
                        currentProgress = value.value,
                        name = 0,
                    )
                }

            statsList.add(
                StatsChartUiModel(
                    maxProgress = maxProgress,
                    items = scores,
                ),
            )
        }

        if (account.scores.isNotEmpty()) {
            val maxProgress = account.scores.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_scores),
                ),
            )
            val scores = account
                .scores
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

        return statsList
    }
}
