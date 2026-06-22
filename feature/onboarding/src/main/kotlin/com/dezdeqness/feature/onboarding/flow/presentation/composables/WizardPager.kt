package com.dezdeqness.feature.onboarding.flow.presentation.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dezdeqness.feature.onboarding.flow.presentation.MIN_SELECTED_GENRES
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingStep
import com.dezdeqness.feature.onboarding.flow.presentation.notifications.NotificationsUiState
import com.dezdeqness.feature.onboarding.flow.presentation.wizardSteps
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresUiState

@Composable
fun WizardPager(
    pagerState: PagerState,
    genresState: SelectGenresUiState,
    notificationsState: NotificationsUiState,
    onGenreClick: (String) -> Unit,
    onNotificationsToggled: (Boolean) -> Unit,
    onTimeChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        when (wizardSteps[page]) {
            OnboardingStep.NOTIFICATIONS -> NotificationsStep(
                notificationsEnabled = notificationsState.enabled,
                hours = notificationsState.time.hours,
                minutes = notificationsState.time.minutes,
                onToggle = onNotificationsToggled,
                onTimeChanged = onTimeChanged,
            )

            else -> GenresStep(
                genres = genresState.genres,
                selectedIds = genresState.selectedGenres,
                minSelection = MIN_SELECTED_GENRES,
                onGenreClick = onGenreClick,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
