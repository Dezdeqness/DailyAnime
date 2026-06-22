package com.dezdeqness.presentation.features.useroboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingActions
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingEvent
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingFlowPage
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingStep
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingType
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingUiState
import com.dezdeqness.feature.onboarding.flow.presentation.OnboardingViewModel
import com.dezdeqness.feature.onboarding.flow.presentation.notifications.NotificationsViewModel
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresActions
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresContentPage
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresEvent
import com.dezdeqness.feature.onboarding.selectgenres.presentation.SelectGenresViewModel
import com.dezdeqness.foundation.utils.collectEvents
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Composable
fun OnboardingStandalonePage(
    type: OnboardingType,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val onboardingComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .onboardingComponent()
            .create()
    }
    val factory = onboardingComponent.viewModelFactory()
    val genresViewModel = viewModel<SelectGenresViewModel>(factory = factory)

    when (type) {
        OnboardingType.SelectGenres -> {
            val actions = remember {
                object : SelectGenresActions {
                    override fun onBackPressed() = onFinished()
                    override fun onGenreClicked(genreId: String) =
                        genresViewModel.onGenreClick(genreId)

                    override fun onSaveClicked() = genresViewModel.onSaveClick()
                }
            }

            SelectGenresContentPage(
                modifier = modifier,
                stateFlow = genresViewModel.uiState,
                actions = actions,
            )

            genresViewModel.events.collectEvents { event ->
                when (event) {
                    is SelectGenresEvent.Close -> onFinished()
                }
            }
        }

        OnboardingType.Full -> {
            val coordinator = viewModel<OnboardingViewModel>(factory = factory)
            val notificationsViewModel = viewModel<NotificationsViewModel>(factory = factory)
            val scope = rememberCoroutineScope()

            val stateFlow = remember(coordinator, genresViewModel, notificationsViewModel) {
                combine(
                    coordinator.step,
                    genresViewModel.uiState,
                    notificationsViewModel.uiState,
                ) { step, genres, notifications ->
                    OnboardingUiState(step = step, genres = genres, notifications = notifications)
                }.stateIn(scope, SharingStarted.Eagerly, OnboardingUiState())
            }

            val actions = remember {
                object : OnboardingActions {
                    override fun onContinue() {
                        when (coordinator.step.value) {
                            OnboardingStep.GENRES -> genresViewModel.saveSelection()
                            OnboardingStep.NOTIFICATIONS -> notificationsViewModel.save()
                            else -> Unit
                        }
                        coordinator.onNext()
                    }

                    override fun onSkip() = coordinator.onSkipClicked()
                    override fun onBack() = coordinator.onBackClicked()
                    override fun onFinish() = coordinator.onFinishClicked()
                    override fun onSwipedTo(step: OnboardingStep) = coordinator.onSwipedTo(step)
                    override fun onGenreClick(genreId: String) =
                        genresViewModel.onGenreClick(genreId)

                    override fun onNotificationsToggled(enabled: Boolean) =
                        notificationsViewModel.onToggled(enabled)

                    override fun onTimeChanged(hours: Int, minutes: Int) =
                        notificationsViewModel.onTimeChanged(hours, minutes)
                }
            }

            OnboardingFlowPage(
                modifier = modifier,
                stateFlow = stateFlow,
                actions = actions,
            )

            coordinator.events.collectEvents { event ->
                when (event) {
                    is OnboardingEvent.Finish -> onFinished()
                }
            }
        }
    }
}
