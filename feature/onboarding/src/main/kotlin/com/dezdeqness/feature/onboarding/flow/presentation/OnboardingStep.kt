package com.dezdeqness.feature.onboarding.flow.presentation

import androidx.annotation.StringRes
import com.dezdeqness.feature.onboarding.R

const val MIN_SELECTED_GENRES = 3

enum class OnboardingStep {
    WELCOME,
    GENRES,
    NOTIFICATIONS,
    DONE,
}

enum class OnboardingPhase { WELCOME, WIZARD, DONE }

val wizardSteps = listOf(OnboardingStep.GENRES, OnboardingStep.NOTIFICATIONS)

val OnboardingStep.phase: OnboardingPhase
    get() = when (this) {
        OnboardingStep.WELCOME -> OnboardingPhase.WELCOME
        OnboardingStep.GENRES, OnboardingStep.NOTIFICATIONS -> OnboardingPhase.WIZARD
        OnboardingStep.DONE -> OnboardingPhase.DONE
    }

val OnboardingStep.wizardProgress: Int
    get() = wizardSteps.indexOf(this) + 1

@get:StringRes
val OnboardingStep.actionTitleRes: Int
    get() = when (this) {
        OnboardingStep.WELCOME -> R.string.onboarding_welcome_start
        OnboardingStep.GENRES -> R.string.onboarding_continue
        OnboardingStep.NOTIFICATIONS -> R.string.onboarding_notifications_done
        OnboardingStep.DONE -> R.string.onboarding_done_continue
    }
