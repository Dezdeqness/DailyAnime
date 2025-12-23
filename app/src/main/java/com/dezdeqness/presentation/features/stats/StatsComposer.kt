package com.dezdeqness.presentation.features.stats

import com.dezdeqness.R
import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.core.provider.ResourceProvider
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.ScoreChartUiModel
import com.dezdeqness.presentation.models.StatsChartUiModel
import com.dezdeqness.presentation.models.StatsData
import com.dezdeqness.presentation.models.StatsHeaderUiModel
import com.google.common.collect.ImmutableList

class StatsComposer(
    private val resourceProvider: ResourceProvider,
) {

    fun compose(account: AccountEntity): List<AdapterItem> {
        val statsList = mutableListOf<AdapterItem>()

        if (account.types.isNotEmpty()) {
            val maxProgress = account.types.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_types)
                )
            )

            val scores = account
                .types
                .map { value ->
                    StatsData(
                        textName = value.name,
                        value = value.value.toString(),
                        currentProgress = value.value,
                        name = 0
                    )
                }

            statsList.add(
                StatsChartUiModel(
                    maxProgress = maxProgress,
                    ImmutableList.copyOf(scores)
                )
            )
        }

        if (account.scores.isNotEmpty()) {
            val maxProgress = account.scores.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_scores)
                )
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
                    ImmutableList.copyOf(scores)
                )
            )
        }

        return statsList
    }

}
