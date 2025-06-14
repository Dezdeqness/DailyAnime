package com.dezdeqness.presentation.features.screenshotsviewer

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Command
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Effect
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.Event
import com.dezdeqness.presentation.features.screenshotsviewer.store.ScreenshotsNamespace.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class ScreenshotsViewModel @Inject constructor(
    private val store: ElmStore<Event, State, Effect, Command>,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {
    override val viewModelTag = "ScreenshotsViewModel"

    val state: StateFlow<State> = store.states

    val effects: Flow<Effect> = store.effects

    fun onShareButtonClicked() {
        store.accept(event = Event.ShareUrlClicked)
    }

    fun onScreenShotChanged(index: Int) {
        store.accept(event = Event.IndexChanged(index))
    }

}
