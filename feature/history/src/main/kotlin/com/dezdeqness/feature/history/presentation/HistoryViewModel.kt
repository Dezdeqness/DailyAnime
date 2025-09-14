package com.dezdeqness.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.core.message.BaseMessageProvider
import com.dezdeqness.core.message.MessageConsumer
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val store: ElmStore<HistoryNamespace.Event, HistoryNamespace.State, HistoryNamespace.Effect, HistoryNamespace.Command>,
    private val messageConsumer: MessageConsumer,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val messageProvider: BaseMessageProvider,
) : ViewModel() {

    val state = store
        .states
        .onStart {
            store.accept(HistoryNamespace.Event.InitialLoad)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryNamespace.State()
        )

    val effects = store.effects

    fun onPullDownRefreshed() {
        store.accept(HistoryNamespace.Event.Refresh)
    }

    fun onLoadMore() {
        store.accept(HistoryNamespace.Event.LoadMore)
    }

    fun onErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }
}
