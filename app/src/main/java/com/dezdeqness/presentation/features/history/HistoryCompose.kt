package com.dezdeqness.presentation.features.history

import com.dezdeqness.domain.model.HistoryEntity
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.HistoryHeaderUiModel
import com.dezdeqness.presentation.models.HistoryUiModel
import javax.inject.Inject

class HistoryCompose @Inject constructor() {

    fun compose(items: List<HistoryEntity>): List<AdapterItem> {
        val uiItems = mutableListOf<AdapterItem>()
        var previousHeader = ""
        items.forEach { item ->
            val header = item.createdAt.take(10)

            if (previousHeader != header) {
                previousHeader = header
                uiItems.add(HistoryHeaderUiModel(header))
            }
            uiItems.add(
                HistoryUiModel(
                    name = item.itemRussian.ifEmpty { item.itemName },
                    action = item.description,
                )
            )
        }
        return uiItems
    }

}
