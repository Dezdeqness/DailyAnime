package com.dezdeqness.presentation.features.home.model

data class HomeCalendarSectionUiModel(
    val items: List<HomeCalendarUiModel> = listOf(),
    val status: SectionStatus = SectionStatus.Initial,
)

data class HomeCalendarUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
)
