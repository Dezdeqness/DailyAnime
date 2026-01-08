package com.dezdeqness.feature.settings.store.core

import com.dezdeqness.feature.settings.store.core.SettingsNamespace.DialogState.DialogPayload
import com.dezdeqness.feature.settings.store.models.SettingUiPref

interface SettingsNamespace {
    data class State(
        val settings: List<SettingUiPref> = emptyList(),
        val dialogState: DialogState = DialogState.None,
    )

    interface Event

    interface Effect

    interface Command

    sealed interface DialogState {
        data class ShowModal(val payload: DialogPayload) : DialogState

        data object None : DialogState

        interface DialogPayload
    }

}

data object InitialLoad: SettingsNamespace.Event

data class OnSettingUpdated(val setting: SettingUiPref) : SettingsNamespace.Event
data class CustomEvent(val id: String) : SettingsNamespace.Event

data class OpenDialog(val payload: DialogPayload) : SettingsNamespace.Event

data object CloseDialog : SettingsNamespace.Event

data class OnInitialStateLoaded(val settings: List<SettingUiPref>) : SettingsNamespace.Event
data class OnSettingClicked(val id: String) : SettingsNamespace.Event

data class OnSettingSwitchChanged(
    val id: String,
    val checked: Boolean
) : SettingsNamespace.Event
