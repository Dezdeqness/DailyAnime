package com.dezdeqness.feature.settings.store.actors

import com.dezdeqness.feature.settings.store.core.SettingUiPref
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.models.SectionType

interface SectionActor {
    val sectionType: SectionType

    fun canHandle(sectionType: SectionType): Boolean = this.sectionType == sectionType

    suspend fun buildSettings(): List<SettingUiPref>

    suspend fun handleClick(settingId: String, currentSetting: SettingUiPref): ActorResult

    suspend fun handleSwitchChange(
        settingId: String,
        checked: Boolean,
        currentSetting: SettingUiPref,
    ) =
        ActorResult()
}

data class ActorResult(
    val updatedSettings: List<SettingUiPref>? = null,
    val effect: SettingsNamespace.Effect? = null,
    val dialog: SettingsNamespace.DialogState? = null,
)
