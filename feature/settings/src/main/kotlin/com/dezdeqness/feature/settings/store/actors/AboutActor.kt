package com.dezdeqness.feature.settings.store.actors

import com.dezdeqness.feature.settings.R
import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.models.SectionType
import com.dezdeqness.shared.presentation.bridge.ApplicationBridge
import javax.inject.Inject

const val VERSION_ID = "version"
const val DEBUG_OPTION_ID = "debug_option"
const val ABOUT_HEADER_ID = "about_header"

data object OpenDebugMenu : SettingsNamespace.Effect

class AboutActor @Inject constructor(
    private val applicationBridge: ApplicationBridge,
) : SectionActor {

    override val sectionType = SectionType.About

    override suspend fun buildSettings(): List<SettingUiPref> {
        val items = mutableListOf(
            SettingUiPref.HeaderSetting(
                id = ABOUT_HEADER_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_about_section
            ),

            SettingUiPref.ActionLessSetting(
                id = VERSION_ID,
                sectionType = sectionType,
                titleResId = R.string.settings_version_title,
                subtitle = applicationBridge.getVersionName()
            )
        )

        if (applicationBridge.isDebug()) {
            items.add(
                SettingUiPref.ActionSetting(
                    id = DEBUG_OPTION_ID,
                    sectionType = sectionType,
                    titleResId = R.string.settings_debug_option,
                    isPostfixShown = true
                )
            )
        }

        return items
    }

    override suspend fun handleClick(settingId: String, currentSetting: SettingUiPref): ActorResult {
        when (settingId) {
            DEBUG_OPTION_ID -> {
                return ActorResult(effect = OpenDebugMenu)
            }
        }
        return ActorResult()
    }
}
