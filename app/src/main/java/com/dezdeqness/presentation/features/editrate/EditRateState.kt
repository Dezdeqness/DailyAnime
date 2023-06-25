package com.dezdeqness.presentation.features.editrate

import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.models.UserRateStatusUiModel

data class EditRateState(
    val rateId: Long = -1,
    val title: String = "",
    val status: UserRateStatusUiModel = UserRateStatusUiModel("", ""),
    val score: Long = 0,
    val episode: Long = 0,
    val isUserRateChanged: Boolean = false,
    val events: List<Event> = listOf(),
    val carouselUiModels: List<CarouselUiModel> = listOf()
)
