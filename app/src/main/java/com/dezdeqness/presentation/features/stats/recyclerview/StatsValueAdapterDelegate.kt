package com.dezdeqness.presentation.features.stats.recyclerview

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemStatsValueBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.StatsValueUiModel

fun statsValueAdapterDelegate() =
    adapterDelegateViewBinding<StatsValueUiModel, AdapterItem, ItemStatsValueBinding>(
        modelClass = StatsValueUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemStatsValueBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    statsName.text = item.name
                    statsValue.text = item.value
                    statsProgress.max = item.maxProgress
                    statsProgress.progress = item.currentProgress
                }
            }
        }
    )
