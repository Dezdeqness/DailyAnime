package com.dezdeqness.feature.settings.store.core

import java.util.Collections.emptyList

interface SettingsNamespace {
    data class State(
        val settings: List<SettingUiPref> = emptyList(),
        val isAuthorized: Boolean = false,
        val dialogState: DialogState? = null,
    )

    interface Event

    interface Effect

    interface Command

    sealed interface DialogState {
        data class ShowModal(val payload: DialogPayload, val settingId: String) : DialogState

        data object None : DialogState

        interface DialogPayload
        interface DialogResult
    }

}

data object InitialLoad : SettingsNamespace.Event

data class OnSettingUpdated(val setting: SettingUiPref) : SettingsNamespace.Event
data class CustomEvent(val id: String) : SettingsNamespace.Event

data class ShowDialog(val dialogState: SettingsNamespace.DialogState) : SettingsNamespace.Event

data object CloseDialog : SettingsNamespace.Event

data class OnDialogResult(
    val id: String,
    val data: SettingsNamespace.DialogState.DialogResult,
) : SettingsNamespace.Event

data object Invalidate : SettingsNamespace.Event

data class OnInitialStateLoaded(val settings: List<SettingUiPref>) : SettingsNamespace.Event
data class OnSettingClicked(val id: String) : SettingsNamespace.Event

data class OnSettingSwitchChanged(
    val id: String,
    val checked: Boolean
) : SettingsNamespace.Event

data class DeployEffect(val effect: SettingsNamespace.Effect) : SettingsNamespace.Event

data object LoadInitialState : SettingsNamespace.Command

data class HandleSettingClick(
    val id: String,
    val setting: SettingUiPref,
) : SettingsNamespace.Command

data class OnEvent(
    val event: SettingsNamespace.Event,
    val setting: SettingUiPref,
) : SettingsNamespace.Command

data class HandleSwitchChange(
    val id: String,
    val checked: Boolean,
    val setting: SettingUiPref,
) : SettingsNamespace.Command

data class SaveDialogResult(
    val id: String,
    val data: SettingsNamespace.DialogState.DialogResult,
    val currentSetting: SettingUiPref,
) : SettingsNamespace.Command
