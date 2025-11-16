package com.dezdeqness.presentation.features.settings

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.models.ImageCacheMaxSizePreference
import com.dezdeqness.contract.settings.models.InitialSection
import com.dezdeqness.contract.settings.models.InitialSectionPreference
import com.dezdeqness.contract.settings.models.NightThemePreference
import com.dezdeqness.contract.settings.models.NotificationEnabledPreference
import com.dezdeqness.contract.settings.models.NotificationTimePreference
import com.dezdeqness.contract.settings.models.StatusesOrderPreference
import com.dezdeqness.contract.settings.models.TimeEntity
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.WorkSchedulerManager
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.ui.TimeData
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.AlarmManagerProvider
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.data.provider.PermissionCheckProvider
import com.dezdeqness.data.provider.StatusesProvider
import com.dezdeqness.presentation.event.OpenSelectGenresPage
import com.dezdeqness.presentation.event.OpenSettingsAlarm
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
    private val alarmManagerProvider: AlarmManagerProvider,
    private val homeGenresProvider: HomeGenresProvider,
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
            val themeStatus = settingsRepository.getPreference(NightThemePreference)
            val section = settingsRepository.getPreference(InitialSectionPreference)
            val isAuthorized = authRepository.isAuthorized()
            val statuses = statusesProvider.getStatuses().associateBy { it.groupedId }
            val isNotificationsTurnOn =
                settingsRepository.getPreference(NotificationEnabledPreference) && permissionCheckProvider.isNotificationPermissionGranted()
            val notificationTime = settingsRepository.getPreference(NotificationTimePreference)
            val maxImageCacheSize = settingsRepository.getPreference(ImageCacheMaxSizePreference)
            val isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference)

            val orderedStatuses = (
                    settingsRepository
                        .getPreference(StatusesOrderPreference)
                        .ifEmpty {
                            statusesProvider.getStatuses().map { it.groupedId }
                        }
                    ).mapNotNull { statuses[it] }
            val selectedInterests = homeGenresProvider.getHomeSectionGenres().map { it.name }

            _settingsStateFlow.update {
                it.copy(
                    isDarkThemeEnabled = themeStatus,
                    selectedSection = SelectSectionItem.getById(section.id),
                    isAuthorized = isAuthorized,
                    isNotificationsTurnOn = isNotificationsTurnOn && alarmManagerProvider.canScheduleExactAlarms(),
                    isNotificationsEnabled = permissionCheckProvider.isNotificationPermissionGranted(),
                    notificationTimeData = TimeData(
                        hours = notificationTime.hours,
                        minutes = notificationTime.minutes
                    ),
                    personalRibbonStatuses = ImmutableList.copyOf(orderedStatuses.map(ribbonMapper::map)),
                    isCalendarEnabled = configManager.isCalendarEnabled,
                    maxImageCacheSize = maxImageCacheSize,
                    isAdultContentEnabled = isAdultContentEnabled,
                    selectedInterests = ImmutableList.copyOf(selectedInterests)
                )
            }
        }
    }

    override val viewModelTag = "SettingsViewModel"

    fun onNightThemeToggleChecked(isChecked: Boolean) {
        val isEnabled = _settingsStateFlow.value.isDarkThemeEnabled
        if (isChecked == isEnabled) return

        launchOnMain {
            settingsRepository.setPreference(NightThemePreference, isChecked)
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
            settingsRepository.setPreference(InitialSectionPreference, section)

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

            settingsRepository.setPreference(StatusesOrderPreference, statusesIds)
            val statuses = statusesProvider.getStatuses().associateBy { it.groupedId }

            val orderedStatuses = settingsRepository.getPreference(StatusesOrderPreference)
                .mapNotNull { statuses[it] }

            _settingsStateFlow.update {
                it.copy(
                    personalRibbonStatuses = ImmutableList.copyOf(orderedStatuses.map(ribbonMapper::map))
                )
            }
        }
    }

    fun onChangeAdultContentClicked(isEnabled: Boolean) {
        launchOnIo {
            settingsRepository.setPreference(AdultContentPreference, isEnabled)

            _settingsStateFlow.update {
                it.copy(isAdultContentEnabled = isEnabled)
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
            settingsRepository.setPreference(NotificationEnabledPreference, isEnabled)

            if (!alarmManagerProvider.canScheduleExactAlarms()) {
                onEventReceive(OpenSettingsAlarm)
            }

            _settingsStateFlow.update {
                it.copy(
                    isNotificationsTurnOn =
                        isEnabled
                                && permissionCheckProvider.isNotificationPermissionGranted()
                                && alarmManagerProvider.canScheduleExactAlarms()
                )
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
            settingsRepository.setPreference(NotificationTimePreference, TimeEntity(hours, minutes))
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

    fun onMaxImageCacheSizeClicked() {
        _settingsStateFlow.update {
            it.copy(isMaxImageCacheSizeDialogShown = true)
        }
    }

    fun onMaxImageCacheSize(size: Int) {
        launchOnIo {
            settingsRepository.setPreference(ImageCacheMaxSizePreference, size)
            _settingsStateFlow.update {
                it.copy(
                    isMaxImageCacheSizeDialogShown = false,
                    maxImageCacheSize = size,
                )
            }
        }
    }

    fun onMaxImageCacheSizeDialogClosed() {
        _settingsStateFlow.update {
            it.copy(isMaxImageCacheSizeDialogShown = false)
        }
    }

    fun invalidate() {
        val selectedInterests = homeGenresProvider.getHomeSectionGenres().map { it.name }
        _settingsStateFlow.update {
            it.copy(
                isNotificationsTurnOn = alarmManagerProvider.canScheduleExactAlarms(),
                selectedInterests = ImmutableList.copyOf(selectedInterests),
            )
        }
    }

    fun onSelectInterestsClicked() {
        onEventReceive(OpenSelectGenresPage)
    }
}
