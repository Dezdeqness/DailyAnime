package com.dezdeqness.presentation.features.settings

import com.dezdeqness.R
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.repository.SettingsRepository
import com.dezdeqness.presentation.event.SwitchDarkTheme
import com.dezdeqness.presentation.features.personallist.PersonalRibbonMapper
import com.dezdeqness.presentation.features.settings.composables.SelectSectionItem
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val statusesProvider: StatusesProvider,
    private val ribbonMapper: PersonalRibbonMapper,
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
        launchOnIo {
            val themeStatus = settingsRepository.getNightThemeStatus()
            val sectionId = settingsRepository.getSelectedInitialSection() ?: R.id.home_nav_graph
            val isAuthorized = accountRepository.isAuthorized()
            val statuses = statusesProvider.getStatuses().associateBy { it.groupedId }

            val orderedStatuses = settingsRepository.getStatusesOrder().mapNotNull { statuses[it] }

            _settingsStateFlow.update {
                it.copy(
                    isDarkThemeEnabled = themeStatus,
                    selectedSection = SelectSectionItem.getById(sectionId),
                    isAuthorized = isAuthorized,
                    personalRibbonStatuses = ImmutableList.copyOf(orderedStatuses.map(ribbonMapper::map))
                )
            }
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

    fun onChangeRibbonStatusClicked() {
        _settingsStateFlow.update {
            it.copy(isStatusReorderDialogShown = true)
        }
    }

    fun onSelectedRibbonDataChanged(statuses: List<RibbonStatusUiModel>) {
        launchOnIo {
            val statusesIds = statuses.map { it.id }

            settingsRepository.setStatusesOrder(statusesIds)
            val statuses = statusesProvider.getStatuses().associateBy { it.groupedId }

            val orderedStatuses = settingsRepository.getStatusesOrder().mapNotNull { statuses[it] }

            _settingsStateFlow.update {
                it.copy(
                    personalRibbonStatuses = ImmutableList.copyOf(orderedStatuses.map(ribbonMapper::map))
                )
            }
        }
    }

    fun onChangeRibbonStatusClosed() {
        _settingsStateFlow.update {
            it.copy(isStatusReorderDialogShown = false)
        }
    }

}
