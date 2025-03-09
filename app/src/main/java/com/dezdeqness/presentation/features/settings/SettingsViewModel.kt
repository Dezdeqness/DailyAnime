package com.dezdeqness.presentation.features.settings

import com.dezdeqness.R
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.presentation.event.SwitchDarkTheme
import com.dezdeqness.presentation.features.settings.composables.SelectSectionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
            val sectionId = settingsRepository.getSelectedInitialSection() ?: R.id.home_nav_graph

            _settingsStateFlow.value =
                _settingsStateFlow.value.copy(
                    isDarkThemeEnabled = themeStatus,
                    selectedSection = SelectSectionItem.getById(sectionId),
                )
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

    fun onChangeInitialSectionClicked() {
        _settingsStateFlow.update {
            it.copy(isSelectInitialSectionDialogShown = true)
        }
    }

    fun onSelectedSectionChanged(sectionId: Int) {
        launchOnIo {
            settingsRepository.setSelectedInitialSection(sectionId)

            val sectionItem = SelectSectionItem.getById(sectionId)
            _settingsStateFlow.update {
                it.copy(selectedSection = sectionItem)
            }
        }

    }

    fun onSelectedSectionDialogClosed() {
        _settingsStateFlow.update {
            it.copy(isSelectInitialSectionDialogShown = false)
        }
    }

}
