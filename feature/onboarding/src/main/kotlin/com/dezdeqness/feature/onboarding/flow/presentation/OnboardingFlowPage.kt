package com.dezdeqness.feature.onboarding.flow.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.feature.onboarding.flow.presentation.composables.DoneStep
import com.dezdeqness.feature.onboarding.flow.presentation.composables.OnboardingBottomBar
import com.dezdeqness.feature.onboarding.flow.presentation.composables.OnboardingHeader
import com.dezdeqness.feature.onboarding.flow.presentation.composables.WelcomeStep
import com.dezdeqness.feature.onboarding.flow.presentation.composables.WizardPager
import com.dezdeqness.foundation.ui.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun OnboardingFlowPage(
    stateFlow: StateFlow<OnboardingUiState>,
    actions: OnboardingActions,
    modifier: Modifier = Modifier,
) {
    val state by stateFlow.collectAsStateWithLifecycle()
    val step = state.step

    val pagerState = rememberPagerState(pageCount = { wizardSteps.size })

    LaunchedEffect(step) {
        if (step.phase == OnboardingPhase.WIZARD) {
            val target = wizardSteps.indexOf(step)
            if (pagerState.currentPage != target) {
                pagerState.animateScrollToPage(target)
            }
        }
    }
    LaunchedEffect(pagerState.settledPage) {
        val current = stateFlow.value.step
        if (current.phase != OnboardingPhase.WIZARD) return@LaunchedEffect
        val settledStep = wizardSteps[pagerState.settledPage]
        when {
            settledStep == current -> Unit
            settledStep.ordinal > current.ordinal -> actions.onContinue()
            else -> actions.onSwipedTo(settledStep)
        }
    }

    BackHandler(enabled = step != OnboardingStep.WELCOME) {
        actions.onBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppTheme.colors.onPrimary,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = contentPadding.calculateBottomPadding()),
        ) {
            AnimatedVisibility(
                visible = step != OnboardingStep.WELCOME,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                OnboardingHeader(
                    step = step,
                    onBack = actions::onBack,
                    onClose = actions::onFinish,
                )
            }

            AnimatedContent(
                targetState = step.phase,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                transitionSpec = {
                    if (targetState.ordinal >= initialState.ordinal) {
                        (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
                    }
                },
                label = "onboarding_phase",
            ) { phase ->
                when (phase) {
                    OnboardingPhase.WELCOME -> WelcomeStep()

                    OnboardingPhase.WIZARD -> WizardPager(
                        pagerState = pagerState,
                        genresState = state.genres,
                        notificationsState = state.notifications,
                        onGenreClick = actions::onGenreClick,
                        onNotificationsToggled = actions::onNotificationsToggled,
                        onTimeChanged = actions::onTimeChanged,
                    )

                    OnboardingPhase.DONE -> DoneStep(
                        selectedGenreNames = state.genres.selectedGenreNames,
                        notificationsEnabled = state.notifications.enabled,
                        notificationTimeLabel = state.notifications.timeLabel,
                    )
                }
            }

            OnboardingBottomBar(
                step = step,
                genresValid = state.genresValid,
                onSkip = actions::onSkip,
                onContinue = actions::onContinue,
                onFinish = actions::onFinish,
            )
        }
    }
}
