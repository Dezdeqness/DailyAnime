package com.dezdeqness.feature.onboarding.flow.presentation

sealed interface OnboardingEvent {
    data object Finish : OnboardingEvent
}
