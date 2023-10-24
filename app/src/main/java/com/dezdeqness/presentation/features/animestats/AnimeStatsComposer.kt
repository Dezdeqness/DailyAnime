package com.dezdeqness.presentation.features.animestats

import com.dezdeqness.R
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.StatsHeaderUiModel
import com.dezdeqness.presentation.models.StatsValueUiModel


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
            animeStatsFragment.scoresArgument.forEach { value ->
                statsList.add(
                    StatsValueUiModel(
                        name = value.name,
                        value = value.value.toString(),
                        currentProgress = value.value,
                        maxProgress = maxProgress,
                    )
                )
            }
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
            animeStatsFragment.statusesArgument.forEach { value ->
                statsList.add(
                    StatsValueUiModel(
                        name = value.name,
                        value = value.value.toString(),
                        currentProgress = value.value,
                        maxProgress = maxProgress,
                    )
                )
            }
        }

        return statsList
    }

}
