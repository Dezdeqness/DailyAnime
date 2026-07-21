package com.dezdeqness.contract.filter.model

data class SearchSectionUiModel(
    val innerId: String,
    val queryId: String = innerId,
    val displayName: String,
    val items: List<AnimeCell>,
    val selectedCells: Set<String> = setOf(),
    val isExpandable: Boolean = false,
    val sectionType: SectionType = SectionType.ChipMultipleChoice,
)

data class AnimeCell(
    val id: String,
    val displayName: String,
)

enum class SectionType {
    CheckBox,
    ChipSingleChoice,
    ChipMultipleChoice,
}
