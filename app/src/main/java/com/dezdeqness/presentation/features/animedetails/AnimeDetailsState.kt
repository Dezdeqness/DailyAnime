package com.dezdeqness.presentation.features.animedetails

import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.models.AdapterItem

data class AnimeDetailsState(
    val title: String = "",
    val imageUrl: String = "",
    val ratingScore: Float = 0.0f,
    val uiModels: List<AdapterItem> = listOf(),
    val isEditUserRateShowing: Boolean = false,
    val events: List<Event> = listOf(),
)
