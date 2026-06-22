package com.dezdeqness.feature.onboarding.flow.presentation

interface OnboardingActions {
    fun onContinue()
    fun onSkip()
    fun onBack()
    fun onFinish()
    fun onSwipedTo(step: OnboardingStep)
    fun onGenreClick(genreId: String)
    fun onNotificationsToggled(enabled: Boolean)
    fun onTimeChanged(hours: Int, minutes: Int)
}
