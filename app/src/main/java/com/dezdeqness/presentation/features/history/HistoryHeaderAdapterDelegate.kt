package com.dezdeqness.presentation.features.history

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemHistoryHeaderBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.HistoryHeaderUiModel

fun historyHeaderAdapterDelegate() =
    adapterDelegateViewBinding<HistoryHeaderUiModel, AdapterItem, ItemHistoryHeaderBinding>(
        modelClass = HistoryHeaderUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemHistoryHeaderBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    header.text = item.title
                }
            }
        }
    )
