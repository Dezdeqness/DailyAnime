package com.dezdeqness.presentation.features.stats

import com.dezdeqness.R
import com.dezdeqness.data.provider.ResourceProvider
import com.dezdeqness.domain.model.AccountEntity
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.StatsHeaderUiModel
import com.dezdeqness.presentation.models.StatsValueUiModel

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
            account.types.forEach { value ->
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

        if (account.scores.isNotEmpty()) {
            val maxProgress = account.scores.sumOf {
                it.value
            }

            statsList.add(
                StatsHeaderUiModel(
                    header = resourceProvider.getString(R.string.stats_header_scores)
                )
            )
            account.scores.forEach { value ->
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