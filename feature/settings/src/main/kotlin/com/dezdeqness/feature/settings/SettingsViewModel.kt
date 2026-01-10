package com.dezdeqness.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.feature.settings.store.core.CloseDialog
import com.dezdeqness.feature.settings.store.core.InitialLoad
import com.dezdeqness.feature.settings.store.core.Invalidate
import com.dezdeqness.feature.settings.store.core.OnSettingClicked
import com.dezdeqness.feature.settings.store.core.OnSettingSwitchChanged
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.Command
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.Effect
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.Event
import com.dezdeqness.feature.settings.store.core.SettingsNamespace.State
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val store: ElmStore<Event, State, Effect, Command>,
) : ViewModel() {

    val state = store
        .states
        .onStart {
            store.accept(InitialLoad)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = State()
        )

    val effects = store.effects

    fun onSettingClicked(id: String) {
        store.accept(OnSettingClicked(id))
    }

    fun onSwitchChanged(id: String, checked: Boolean) {
        store.accept(OnSettingSwitchChanged(id, checked))
    }

    fun onDialogClosed() {
        store.accept(CloseDialog)
    }
//
//    fun onDialogResult(result: SettingsNamespace.DialogResult) {
//        store.accept(OnDialogResult(result))
//    }

    fun invalidate() {
        store.accept(Invalidate)
    }

}
