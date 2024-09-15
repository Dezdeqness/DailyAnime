package com.dezdeqness.presentation.features.settings

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.presentation.event.SwitchDarkTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _settingsStateFlow: MutableStateFlow<SettingsState> =
        MutableStateFlow(SettingsState())
    val settingsStateFlow: StateFlow<SettingsState> get() = _settingsStateFlow

    init {
        launchOnMain {
            val themeStatus = settingsRepository.getNightThemeStatus()
            _settingsStateFlow.value =
                _settingsStateFlow.value.copy(isDarkThemeEnabled = themeStatus)
        }
    }

    override val viewModelTag = "SettingsViewModel"

    fun onNightThemeToggleChecked(isChecked: Boolean) {
        val isEnabled = _settingsStateFlow.value.isDarkThemeEnabled
        if (isChecked == isEnabled) return

        launchOnMain {
            settingsRepository.setNightThemeStatus(isChecked)
        }

        _settingsStateFlow.value = _settingsStateFlow.value.copy(
            isDarkThemeEnabled = isChecked,
        )
        onEventReceive(SwitchDarkTheme(isEnabled = isChecked))
    }

}
