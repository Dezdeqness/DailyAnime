package com.dezdeqness.presentation.features.home

data class HomeState(
    val sectionsState: SectionsState = SectionsState()
)

data class SectionsState(
    val sections: List<SectionUiModel> = listOf(),
    val status: SectionStatus = SectionStatus.Initial,
)

enum class SectionStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
