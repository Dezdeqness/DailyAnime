package com.dezdeqness.feature.onboarding.selectgenres.presentation

sealed interface SelectGenresEvent {
    object Close : SelectGenresEvent
}
