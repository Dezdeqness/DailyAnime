package com.dezdeqness.feature.settings

import com.dezdeqness.feature.settings.store.core.CloseDialog
import com.dezdeqness.feature.settings.store.core.InitialLoad
import com.dezdeqness.feature.settings.store.core.Invalidate
import com.dezdeqness.feature.settings.store.core.OnDialogResult
import com.dezdeqness.feature.settings.store.core.OnSettingClicked
import com.dezdeqness.feature.settings.store.core.OnSettingSwitchChanged
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.Command
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.Effect
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.Event
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.State
import com.dezdeqness.foundation.BaseStoreViewModel
import javax.inject.Inject
import money.vivid.elmslie.core.store.ElmStore

class SettingsViewModel @Inject constructor(
    store: ElmStore<Event, State, Effect, Command>,
) : BaseStoreViewModel<Event, State, Effect, Command>(
    store = store,
    initialState = State(),
    initialEvent = InitialLoad,
) {

    fun onSettingClicked(id: String) {
        accept(OnSettingClicked(id))
    }

    fun onSwitchChanged(id: String, checked: Boolean) {
        accept(OnSettingSwitchChanged(id, checked))
    }

    fun onDialogClosed() {
        accept(CloseDialog)
    }

    fun onDialogResult(id: String, data: SettingsNamespace.DialogState.DialogResult) {
        accept(OnDialogResult(id = id, data = data))
    }

    fun invalidate() {
        accept(Invalidate)
    }
}
