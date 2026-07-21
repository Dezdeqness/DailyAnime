package com.dezdeqness.feature.searchfilter.presentation.preview

import com.dezdeqness.contract.filter.model.AnimeCell
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.contract.filter.model.SectionType

object SearchFilterPreviewData {

    val chipSection = SearchSectionUiModel(
        innerId = "kind",
        displayName = "Type",
        items = listOf(
            AnimeCell(id = "tv", displayName = "TV Сериал"),
            AnimeCell(id = "movie", displayName = "Фильм"),
            AnimeCell(id = "ova", displayName = "OVA"),
            AnimeCell(id = "ona", displayName = "ONA"),
            AnimeCell(id = "special", displayName = "Спешл"),
        ),
        selectedCells = setOf("tv", "movie"),
        sectionType = SectionType.ChipMultipleChoice,
    )

    val checkboxSection = SearchSectionUiModel(
        innerId = "genre",
        displayName = "Genre",
        items = listOf(
            AnimeCell(id = "1", displayName = "Экшен"),
            AnimeCell(id = "2", displayName = "Приключения"),
            AnimeCell(id = "3", displayName = "Комедия"),
        ),
        selectedCells = setOf("1"),
        isExpandable = true,
        sectionType = SectionType.CheckBox,
    )
}
