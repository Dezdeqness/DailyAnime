package com.dezdeqness.presentation.features.home.composable

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

enum class SectionStatus {
    Initial,
    Loading,
    Error,
    Loaded,
}
