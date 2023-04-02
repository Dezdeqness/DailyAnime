package com.dezdeqness.presentation.features.history

import android.text.Html
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemHistoryCellBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.HistoryUiModel


fun historyAdapterDelegate() =
    adapterDelegateViewBinding<HistoryUiModel, AdapterItem, ItemHistoryCellBinding>(
        modelClass = HistoryUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemHistoryCellBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    name.text = item.name
                    action.text = Html.fromHtml(item.action, FROM_HTML_MODE_LEGACY)
                }
            }
        }
    )
