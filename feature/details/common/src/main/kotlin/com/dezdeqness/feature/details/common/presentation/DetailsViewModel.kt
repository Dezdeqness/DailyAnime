package com.dezdeqness.feature.details.common.presentation

import com.dezdeqness.feature.details.common.presentation.store.DetailsState
import com.dezdeqness.foundation.BaseStoreViewModel
import kotlinx.coroutines.flow.SharingStarted
import money.vivid.elmslie.core.store.ElmStore

open class DetailsViewModel<UiEvent : Any, Event : Any, State : DetailsState, Effect : Any, Command : Any>(
    store: ElmStore<Event, State, Effect, Command>,
    initialState: State,
    initialEvent: Event,
    private val translator: DetailsEventTranslator<UiEvent, Event>,
) : BaseStoreViewModel<Event, State, Effect, Command>(
    store = store,
    initialState = initialState,
    sharingStarted = SharingStarted.Lazily,
    initialEvent = initialEvent,
) {
    fun onUiEvent(uiEvent: UiEvent) {
        accept(translator.translate(uiEvent))
    }
}
