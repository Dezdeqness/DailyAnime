package com.dezdeqness.presentation.features.editrate

import com.dezdeqness.domain.model.UserRateStatusEntity
import com.dezdeqness.presentation.event.Event

data class EditRateState(
    val rateId: Long = 0,
    val title: String = "",
    val status: UserRateStatusEntity = UserRateStatusEntity.UNKNOWN,
    val score: Long = 0,
    val episode: Long = 0,
    val isUserRateChanged: Boolean = false,
    val events: List<Event> = listOf(),
)
