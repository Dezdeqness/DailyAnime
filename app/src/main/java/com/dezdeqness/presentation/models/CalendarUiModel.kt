package com.dezdeqness.presentation.models

data class CalendarListUiModel(
    val header: String,
    val items: List<CalendarUiModel>,
) : AdapterItem()

data class CalendarUiModel(
    val id: Long,
    val name: String,
    val episodeInfo: String,
    val logoUrl: String,
) : AdapterItem()
