package com.dezdeqness.feature.settings.store.actors

import com.dezdeqness.contract.settings.models.NotificationEnabledPreference
import com.dezdeqness.contract.settings.models.NotificationTimePreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.provider.AlarmManagerProvider
import com.dezdeqness.feature.settings.R
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.models.SectionType
import com.dezdeqness.shared.presentation.manager.WorkSchedulerManager
import com.dezdeqness.shared.presentation.provider.PermissionCheckProvider
import java.util.Locale
import javax.inject.Inject

private const val NOTIFICATION_TIME_ID = "notification_time"
private const val NOTIFICATION_ENABLE_ID = "notification_enable"
private const val NOTIFICATION_HEADER_ID = "notification_header"

data object OpenAlarmSettings : SettingsNamespace.Effect

class NotificationActor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val permissionCheckProvider: PermissionCheckProvider,
    private val alarmManagerProvider: AlarmManagerProvider,
    private val workSchedulerManager: WorkSchedulerManager,
) : SectionActor {

    override val sectionType: SectionType = SectionType.Notification

    override suspend fun buildSettings(): List<SettingUiPref> {
        val isEnabled = settingsRepository.getPreference(NotificationEnabledPreference)
        val notificationTime = settingsRepository.getPreference(NotificationTimePreference)
        val canScheduleAlarms = alarmManagerProvider.canScheduleExactAlarms()
        val hasPermission = permissionCheckProvider.isNotificationPermissionGranted()
        val finalEnabled = isEnabled && hasPermission && canScheduleAlarms

        return listOf(
            SettingUiPref.HeaderSetting(
                id = NOTIFICATION_HEADER_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_notification_section
            ),
            SettingUiPref.SwitchSetting(
                id = NOTIFICATION_ENABLE_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_notification_title,
                subtitleResId = R.string.settings_notification_subtitle,
                enabled = hasPermission,
                checked = finalEnabled
            ),
            SettingUiPref.ActionSetting(
                id = NOTIFICATION_TIME_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_notification_time_title,
                subtitle = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    notificationTime.hours,
                    notificationTime.minutes
                ),
                enabled = hasPermission,
            )
        )
    }

    override suspend fun handleClick(
        settingId: String,
        currentSetting: SettingUiPref
    ): ActorResult {
        return ActorResult()
    }

    override suspend fun handleSwitchChange(
        settingId: String,
        checked: Boolean,
        currentSetting: SettingUiPref
    ): ActorResult {
        when (settingId) {
            NOTIFICATION_ENABLE_ID -> {
                settingsRepository.setPreference(NotificationEnabledPreference, checked)

                val canScheduleAlarms = alarmManagerProvider.canScheduleExactAlarms()
                val hasPermission = permissionCheckProvider.isNotificationPermissionGranted()
                val finalEnabled = checked && hasPermission && canScheduleAlarms

                val updated =
                    (currentSetting as SettingUiPref.SwitchSetting).copy(checked = finalEnabled)

                val effect = if (checked && !canScheduleAlarms) {
                    OpenAlarmSettings
                } else null

                return ActorResult(updatedSettings = listOf(updated), effect = effect)
            }
        }
        return ActorResult()
    }
}
