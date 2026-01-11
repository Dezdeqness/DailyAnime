package com.dezdeqness.feature.settings.store.actors

import com.dezdeqness.contract.settings.models.NightThemePreference
import com.dezdeqness.contract.settings.models.ThemeMode
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.feature.settings.R
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.models.SectionType
import com.dezdeqness.feature.settings.utils.fromMode
import javax.inject.Inject

private const val THEME_SELECTOR_ID = "theme_selector"
private const val THEME_HEADER_ID = "theme_header"

data class ThemeSelectPayload(
    val mode: ThemeMode,
) : SettingsNamespace.DialogState.DialogPayload

data class ThemeSelectResult(
    val mode: ThemeMode,
) : SettingsNamespace.DialogState.DialogResult

class ThemeActor @Inject constructor(
    private val settingsRepository: SettingsRepository
) : SectionActor {

    override val sectionType: SectionType = SectionType.Theme

    override suspend fun buildSettings(): List<SettingUiPref> {
        val themeMode = settingsRepository.getPreference(NightThemePreference)

        return listOf(
            SettingUiPref.HeaderSetting(
                id = THEME_HEADER_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_theme_section,
            ),
            SettingUiPref.ActionSetting(
                id = THEME_SELECTOR_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_dark_theme_title,
                subtitleResId = themeMode.fromMode(),
            )
        )
    }

    override suspend fun handleClick(
        settingId: String,
        currentSetting: SettingUiPref
    ): ActorResult {
        when (settingId) {
            THEME_SELECTOR_ID -> {
                val currentMode = settingsRepository.getPreference(NightThemePreference)
                return ActorResult(
                    dialog = SettingsNamespace.DialogState.ShowModal(
                        payload = ThemeSelectPayload(
                            mode = currentMode,
                        ),
                        settingId = settingId,
                    ),
                )
            }
        }
        return ActorResult()
    }

    override suspend fun saveDialogResult(
        settingId: String,
        data: SettingsNamespace.DialogState.DialogResult,
        currentSetting: SettingUiPref,
    ): ActorResult {
        when (settingId) {
            THEME_SELECTOR_ID -> {
                val mode = (data as ThemeSelectResult).mode

                settingsRepository.setPreference(NightThemePreference, mode)

                val updated = SettingUiPref.ActionSetting(
                    id = THEME_SELECTOR_ID,
                    sectionType = sectionType,
                    titleResId = R.string.settings_dark_theme_title,
                    subtitleResId = mode.fromMode(),
                )

                return ActorResult(updatedSettings = listOf(updated))
            }
        }

        return ActorResult()
    }
}
