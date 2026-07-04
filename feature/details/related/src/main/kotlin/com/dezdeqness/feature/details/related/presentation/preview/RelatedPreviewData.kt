package com.dezdeqness.feature.details.related.presentation.preview

import com.dezdeqness.feature.details.related.presentation.RelatedListActions
import com.dezdeqness.feature.details.related.presentation.models.ChronologyUiModel
import com.dezdeqness.feature.details.related.presentation.models.RelatedListItem
import com.dezdeqness.feature.details.related.presentation.models.SimilarUiModel

object RelatedPreviewData {

    val chronologyItem = ChronologyUiModel(
        id = 1L,
        name = "Fullmetal Alchemist: Brotherhood",
        imageUrl = "",
        briefInfo = "TV Сериал • 2009",
    )

    val similarItem = SimilarUiModel(
        id = 2L,
        name = "Стальной алхимик",
        imageUrl = "",
        briefInfo = "2003 • TV Сериал • 51 эп.",
        score = "8.1",
    )

    val chronologyList: List<RelatedListItem> = listOf(
        chronologyItem,
        ChronologyUiModel(
            id = 3L,
            name = "Fullmetal Alchemist: The Sacred Star of Milos",
            imageUrl = "",
            briefInfo = "Фильм • 2011",
        ),
        ChronologyUiModel(
            id = 4L,
            name = "Fullmetal Alchemist: Premium Collection",
            imageUrl = "",
            briefInfo = "OVA • 2006",
        ),
    )

    val emptyActions = object : RelatedListActions {
        override fun onAnimeClicked(animeId: Long, title: String) = Unit
        override fun onRetryClicked() = Unit
        override fun onBackPressed() = Unit
    }
}
