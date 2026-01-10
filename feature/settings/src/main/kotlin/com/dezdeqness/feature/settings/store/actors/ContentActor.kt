package com.dezdeqness.feature.settings.store.actors

import com.dezdeqness.contract.settings.models.AdultContentPreference
import com.dezdeqness.contract.settings.repository.SettingsRepository
import com.dezdeqness.data.provider.HomeGenresProvider
import com.dezdeqness.feature.settings.R
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.models.SectionType
import javax.inject.Inject

private const val ADULT_ID = "adult"
private const val SELECT_INTERESTS_ID = "select_interests"
private const val CONTENT_HEADER_ID = "content_header"

data object OpenSelectInterests : SettingsNamespace.Effect

class ContentActor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val homeGenresProvider: HomeGenresProvider,
) : SectionActor {

    override val sectionType: SectionType = SectionType.Content

    override suspend fun buildSettings(): List<SettingUiPref> {
        val isAdultContentEnabled = settingsRepository.getPreference(AdultContentPreference)
        val selectedInterests = homeGenresProvider.getHomeSectionGenres().map { it.name }
        val interestsSubtitle = selectedInterests.joinToString(", ")

        return listOf(
            SettingUiPref.HeaderSetting(
                id = CONTENT_HEADER_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_content_section
            ),
            SettingUiPref.ActionSetting(
                id = SELECT_INTERESTS_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_select_interests_title,
                subtitle = interestsSubtitle,
                isPostfixShown = true,
            ),
            SettingUiPref.SwitchSetting(
                id = ADULT_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_adult_content_title,
                checked = !isAdultContentEnabled
            )
        )
    }

    override suspend fun handleClick(
        settingId: String,
        currentSetting: SettingUiPref
    ): ActorResult {
        if (settingId == "select_interests") {
            return ActorResult(effect = OpenSelectInterests)
        }
        return ActorResult()
    }

    override suspend fun handleSwitchChange(
        settingId: String,
        checked: Boolean,
        currentSetting: SettingUiPref
    ): ActorResult {
        when (settingId) {
            ADULT_ID -> {
                settingsRepository.setPreference(AdultContentPreference, !checked)
                val updated =
                    (currentSetting as SettingUiPref.SwitchSetting).copy(checked = checked)
                return ActorResult(updatedSettings = listOf(updated))
            }
        }
        return ActorResult()
    }
}
