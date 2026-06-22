package com.dezdeqness.feature.onboarding.flow.presentation.notifications

import androidx.compose.runtime.Immutable
import com.dezdeqness.contract.settings.models.TimeEntity
import java.util.Locale

@Immutable
data class NotificationsUiState(
    val enabled: Boolean = false,
    val time: TimeEntity = TimeEntity(hours = 19, minutes = 0),
) {
    val timeLabel: String
        get() = String.format(Locale.getDefault(), "%02d:%02d", time.hours, time.minutes)
}
