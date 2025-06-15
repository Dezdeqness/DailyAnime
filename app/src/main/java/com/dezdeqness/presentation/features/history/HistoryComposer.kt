package com.dezdeqness.presentation.features.history

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.dezdeqness.domain.model.HistoryEntity
import com.dezdeqness.presentation.features.history.models.HistoryModel
import com.dezdeqness.presentation.features.history.models.HistoryModel.HistoryHeaderUiModel
import com.dezdeqness.presentation.features.history.models.HistoryModel.HistoryUiModel
import com.dezdeqness.utils.ImageUrlUtils
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
                uiItems.add(HistoryHeaderUiModel(header))
            }
            uiItems.add(
                HistoryUiModel(
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
