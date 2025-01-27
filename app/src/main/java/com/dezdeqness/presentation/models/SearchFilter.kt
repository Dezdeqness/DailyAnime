package com.dezdeqness.presentation.models

import com.google.common.collect.ImmutableList


data class SearchSectionUiModel(
    val innerId: String,
    val displayName: String,
    val items: ImmutableList<AnimeCell>,
    val isExpandable: Boolean = false,
    val sectionType: SectionType = SectionType.ChipMultipleChoice,
)

data class AnimeCell(
    val id: String,
    val displayName: String,
    var state: CellState = CellState.NONE,
)

data class AnimeCellList(
    val list: List<AnimeCell>
) : AdapterItem()

enum class CellState {
    INCLUDE,
    EXCLUDE,
    NONE,
}

enum class SectionType {
    CheckBox,
    ChipSingleChoice,
    ChipMultipleChoice,
}
