package com.dezdeqness.presentation

import com.dezdeqness.contract.settings.models.LanguageDisclaimerPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.provider.LocaleProvider
import com.dezdeqness.presentation.event.LanguageDisclaimer
import com.dezdeqness.presentation.message.MessageConsumer
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
            val locale = localeProvider.getCurrentLocale()
            if (localeUtils.isNonRusLocale(locale)
                && settingsRepository.getPreference(LanguageDisclaimerPreference).not()) {
                onEventReceive(LanguageDisclaimer)
                settingsRepository.setPreference(LanguageDisclaimerPreference, true)
            }
        }

    }
}
