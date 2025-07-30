package com.dezdeqness.presentation.features.settings

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.WorkSchedulerManager
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.ui.TimeData
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.PermissionCheckProvider
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.domain.model.InitialSection
import com.dezdeqness.domain.model.TimeEntity
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
    private val authRepository: AuthRepository,
    private val statusesProvider: StatusesProvider,
    private val ribbonMapper: PersonalRibbonMapper,
    private val permissionCheckProvider: PermissionCheckProvider,
    private val workSchedulerManager: WorkSchedulerManager,
    private val configManager: ConfigManager,
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
            val section = settingsRepository.getSelectedInitialSection()
            val isAuthorized = authRepository.isAuthorized()
            val statuses = statusesProvider.getStatuses().associateBy { it.groupedId }
            val isNotificationsTurnOn = settingsRepository.getNotificationsEnabled()
            val notificationTime = settingsRepository.getNotificationTime()

            val orderedStatuses = settingsRepository.getStatusesOrder().mapNotNull { statuses[it] }

            _settingsStateFlow.update {
                it.copy(
                    isDarkThemeEnabled = themeStatus,
                    selectedSection = SelectSectionItem.getById(section.id),
                    isAuthorized = isAuthorized,
                    isNotificationsTurnOn = isNotificationsTurnOn,
                    isNotificationsEnabled = permissionCheckProvider.isNotificationPermissionGranted(),
                    notificationTimeData = TimeData(
                        hours = notificationTime.hours,
                        minutes = notificationTime.minutes
                    ),
                    personalRibbonStatuses = ImmutableList.copyOf(orderedStatuses.map(ribbonMapper::map)),
                    isCalendarEnabled = configManager.isCalendarEnabled,
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

    fun onSelectedSectionChanged(section: InitialSection) {
        launchOnIo {
            settingsRepository.setSelectedInitialSection(section)

            val sectionItem = SelectSectionItem.getById(section.id)
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

    fun onNotificationToggleClicked(isEnabled: Boolean) {
        launchOnIo {
            settingsRepository.setNotificationsEnabled(isEnabled)

            _settingsStateFlow.update {
                it.copy(isNotificationsTurnOn = isEnabled)
            }
        }
    }

    fun onNotificationTimeClicked() {
        _settingsStateFlow.update {
            it.copy(isNotificationTimePickerDialogShown = true)
        }
    }

    fun onNotificationTimeSaved(hours: Int, minutes: Int) {
        launchOnIo {
            settingsRepository.setNotificationTime(TimeEntity(hours, minutes))
            workSchedulerManager.scheduleDailyWork()
            _settingsStateFlow.update {
                it.copy(
                    isNotificationTimePickerDialogShown = false,
                    notificationTimeData = TimeData(hours = hours, minutes = minutes)
                )
            }
        }
    }

    fun onNotificationTimePickerClosed() {
        _settingsStateFlow.update {
            it.copy(isNotificationTimePickerDialogShown = false)
        }
    }
}
