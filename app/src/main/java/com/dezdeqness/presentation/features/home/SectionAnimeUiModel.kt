package com.dezdeqness.presentation.features.home

data class SectionUiModel(
    val id: String,
    val title: String,
    val items: List<SectionAnimeUiModel>,
)


data class SectionAnimeUiModel(
    val id: Long,
    val title: String,
    val logoUrl: String,
)
