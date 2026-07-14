package com.dezdeqness.feature.home.presentation.models

data class SectionUiModel(
    val id: String,
    val numericId: String,
    val title: String,
    val items: List<SectionAnimeUiModel> = listOf(),
    val status: SectionStatus = SectionStatus.Initial,
)

data class SectionAnimeUiModel(
    val id: Long,
    val title: String,
    val logoUrl: String,
)

data class HomeCalendarSectionUiModel(
    val items: List<HomeCalendarUiModel> = listOf(),
    val status: SectionStatus = SectionStatus.Initial,
    val isCalendarActionVisible: Boolean = false,
)

data class HomeCalendarUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
)

enum class SectionStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
