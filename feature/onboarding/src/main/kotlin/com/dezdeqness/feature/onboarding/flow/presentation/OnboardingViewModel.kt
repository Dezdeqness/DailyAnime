package com.dezdeqness.feature.onboarding.flow.presentation

import com.dezdeqness.contract.settings.models.OnboardingCompletedPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class OnboardingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "OnboardingViewModel"

    private val steps = OnboardingStep.entries

    private val _step = MutableStateFlow(steps.first())
    val step: StateFlow<OnboardingStep> = _step.asStateFlow()

    private val _events = Channel<OnboardingEvent>()
    val events = _events.receiveAsFlow()

    fun onNext() = moveBy(1)

    fun onBackClicked() {
        if (_step.value == OnboardingStep.DONE) return
        moveBy(-1)
    }

    fun onSwipedTo(step: OnboardingStep) {
        _step.value = step
    }

    private fun moveBy(delta: Int) {
        val index = (steps.indexOf(_step.value) + delta).coerceIn(steps.indices)
        _step.value = steps[index]
    }

    fun onSkipClicked() = completeAndFinish()

    fun onFinishClicked() = completeAndFinish()

    private fun completeAndFinish() {
        launchOnIo {
            settingsRepository.setPreference(OnboardingCompletedPreference, true)
            _events.send(OnboardingEvent.Finish)
        }
    }
}
