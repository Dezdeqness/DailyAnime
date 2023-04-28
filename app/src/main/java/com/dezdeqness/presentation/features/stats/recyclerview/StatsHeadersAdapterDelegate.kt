package com.dezdeqness.presentation.features.stats.recyclerview

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemStatsHeaderBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.StatsHeaderUiModel


fun statsHeaderAdapterDelegate() =
    adapterDelegateViewBinding<StatsHeaderUiModel, AdapterItem, ItemStatsHeaderBinding>(
        modelClass = StatsHeaderUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemStatsHeaderBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    statsHeader.text = item.header
                }
            }
        }
    )
