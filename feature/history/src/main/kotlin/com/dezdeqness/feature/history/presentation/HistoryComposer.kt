package com.dezdeqness.feature.history.presentation

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.dezdeqness.contract.history.model.HistoryEntity
import com.dezdeqness.data.utils.ImageUrlUtils
import com.dezdeqness.feature.history.presentation.models.HistoryModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class HistoryComposer @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils
) {

    private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    fun compose(items: List<HistoryEntity>): List<HistoryModel> {
        val uiItems = mutableListOf<HistoryModel>()
        val orderedItems = items.sortedByDescending { it.createdAtTimestamp }
        var previousHeader = ""
        orderedItems.forEach { item ->
            val header = dateFormatter.format(item.createdAtTimestamp)

            if (previousHeader != header) {
                previousHeader = header
                uiItems.add(HistoryModel.HistoryHeaderUiModel(header))
            }
            uiItems.add(
                HistoryModel.HistoryUiModel(
                    name = item.itemRussian.ifEmpty { item.itemName },
                    action = HtmlCompat.fromHtml(
                        item.description,
                        FROM_HTML_MODE_COMPACT,
                    ).toString(),
                    imageUrl = imageUrlUtils.getImageWithBaseUrl(item.image),
                )
            )
        }
        return uiItems
    }

}
