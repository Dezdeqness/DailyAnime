package com.dezdeqness.feature.settings.store.core

import money.vivid.elmslie.core.store.StateReducer

val settingsReducer = object :
    StateReducer<SettingsNamespace.Event, SettingsNamespace.State, SettingsNamespace.Effect, SettingsNamespace.Command>() {
    override fun Result.reduce(event: SettingsNamespace.Event) {
        when (event) {
            is InitialLoad -> {
                commands { +LoadInitialState }
            }

            is OnInitialStateLoaded -> {
                state {
                    SettingsNamespace.State(
                        settings = event.settings,
                        dialogState = SettingsNamespace.DialogState.None,
                    )
                }
            }

            is OnSettingClicked -> {
                val setting = state.settings.firstOrNull { it.id == event.id }
                if (setting != null) {
                    commands { +HandleSettingClick(event.id, setting) }
                }
            }

            is OnSettingSwitchChanged -> {
                val setting = state.settings.firstOrNull { it.id == event.id }
                if (setting != null) {
                    commands { +HandleSwitchChange(event.id, event.checked, setting) }
                }
            }

            is OnSettingUpdated -> {
                state {
                    state.copy(
                        settings = state.settings.map {
                            if (it.id == event.setting.id) event.setting else it
                        }
                    )
                }
            }

            is CustomEvent -> {
                val setting = state.settings.firstOrNull { it.id == event.id }
                if (setting != null) {
                    commands { +OnEvent(event, setting) }
                }
            }

            is OpenDialog -> {
                state {
                    state.copy(
                        dialogState = SettingsNamespace.DialogState.ShowModal(event.payload)
                    )
                }
            }

            is DeployEffect -> {
                effects { +event.effect }
            }

            is CloseDialog -> {
                state {
                    state.copy(
                        dialogState = SettingsNamespace.DialogState.None
                    )
                }
            }

        }
    }
}
