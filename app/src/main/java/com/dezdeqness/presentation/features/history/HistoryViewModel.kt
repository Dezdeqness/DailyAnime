package com.dezdeqness.presentation.features.history

import androidx.lifecycle.viewModelScope
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Command
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Effect
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Event
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.State
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val store: ElmStore<Event, State, Effect, Command>,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: MessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    val state = store
        .states
        .onStart {
            store.accept(Event.InitialLoad)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = State()
        )

    val effects = store.effects

    override val viewModelTag = "HistoryViewModel"

    fun onPullDownRefreshed() {
        store.accept(Event.Refresh)
    }

    fun onLoadMore() {
        store.accept(Event.LoadMore)
    }

    fun onErrorMessage() {
        launchOnIo {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }
}
