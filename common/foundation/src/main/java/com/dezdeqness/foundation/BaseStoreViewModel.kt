package com.dezdeqness.foundation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import money.vivid.elmslie.core.store.ElmStore

abstract class BaseStoreViewModel<Event : Any, State : Any, Effect : Any, Command : Any>(
    protected val store: ElmStore<Event, State, Effect, Command>,
    initialState: State,
    sharingStarted: SharingStarted = SharingStarted.Lazily,
    initialEvent: Event? = null,
) : ViewModel() {

    val state: StateFlow<State> = store
        .states
        .let { flow ->
            if (initialEvent != null) {
                flow.onStart { store.accept(initialEvent) }
            } else {
                flow
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = sharingStarted,
            initialValue = initialState,
        )

    val effects: Flow<Effect> = store.effects

    protected fun accept(event: Event) {
        store.accept(event)
    }
}
