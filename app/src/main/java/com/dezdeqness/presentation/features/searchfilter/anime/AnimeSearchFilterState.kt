package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeSearchFilter

data class AnimeSearchFilterState(
    val items: List<AnimeSearchFilter> = listOf(),
    val currentCellUpdate: AnimeCell? = null,
)
