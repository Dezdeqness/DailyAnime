package com.dezdeqness.feature.onboarding.flow.presentation.notifications

import androidx.compose.runtime.Immutable
import com.dezdeqness.contract.settings.models.TimeEntity

@Immutable
data class NotificationsUiState(
    val enabled: Boolean = false,
    val time: TimeEntity = TimeEntity(hours = 19, minutes = 0),
)
