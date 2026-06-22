package com.dezdeqness.feature.onboarding.flow.presentation

import androidx.compose.runtime.Immutable
import com.dezdeqness.feature.onboarding.flow.presentation.notifications.NotificationsUiState
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresUiState

@Immutable
data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.WELCOME,
    val genres: SelectGenresUiState = SelectGenresUiState(genres = emptyList()),
    val notifications: NotificationsUiState = NotificationsUiState(),
) {
    val genresValid: Boolean
        get() = genres.selectedGenres.size >= MIN_SELECTED_GENRES
}
