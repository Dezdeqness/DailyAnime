package com.dezdeqness.presentation

import com.dezdeqness.contract.settings.models.LanguageDisclaimerPreference
import com.dezdeqness.contract.settings.models.OnboardingCompletedPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.MessageConsumer
import com.dezdeqness.foundation.provider.LocaleProvider
import com.dezdeqness.presentation.event.LanguageDisclaimer
import com.dezdeqness.presentation.event.NavigateToOnboarding
import com.dezdeqness.utils.LocaleUtils
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val localeProvider: LocaleProvider,
    private val localeUtils: LocaleUtils,
    private val settingsRepository: SettingsRepository,
    messageConsumer: MessageConsumer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    val messageState = messageConsumer.messageState

    override val viewModelTag = "MainViewModel"

    init {
        launchOnIo {
            if (settingsRepository.getPreference(OnboardingCompletedPreference).not()) {
                onEventReceive(NavigateToOnboarding)
            }

            val locale = localeProvider.getCurrentLocale()
            if (localeUtils.isNonRusLocale(locale) &&
                settingsRepository.getPreference(LanguageDisclaimerPreference).not()
            ) {
                onEventReceive(LanguageDisclaimer)
                settingsRepository.setPreference(LanguageDisclaimerPreference, true)
            }
        }
    }
}
