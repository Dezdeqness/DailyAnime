package com.dezdeqness.presentation.features.history

import com.dezdeqness.domain.model.HistoryEntity
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.HistoryHeaderUiModel
import com.dezdeqness.presentation.models.HistoryUiModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class HistoryCompose @Inject constructor() {

    private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    fun compose(items: List<HistoryEntity>): List<AdapterItem> {
        val uiItems = mutableListOf<AdapterItem>()
        var previousHeader = ""
        items.forEach { item ->
            val header = dateFormatter.format(item.createdAtTimestamp)

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
