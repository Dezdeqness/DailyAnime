package com.dezdeqness.presentation.features.screenshotsviewer

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.pod.core.store.internal.PlantStore
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ScreenshotsViewModel @Inject constructor(
    private val store: PlantStore<Event, State, Effect>,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {
    override val viewModelTag = "ScreenshotsViewModel"

    val state: StateFlow<State> = store.state

    val effects: Flow<Effect> = store.effects

    init {
        store.onEvent(Event.Init)
    }

    fun onShareButtonClicked() {
        store.onEvent(event = Event.ShareUrlClicked)
    }

    fun onScreenShotChanged(index: Int) {
        store.onEvent(event = Event.IndexChanged(index))
    }

}
