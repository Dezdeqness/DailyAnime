package com.dezdeqness.feature.history.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import com.dezdeqness.feature.history.presentation.store.HistoryNamespace
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    store: ElmStore<HistoryNamespace.Event, HistoryNamespace.State, HistoryNamespace.Effect, HistoryNamespace.Command>,
    private val messageConsumer: MessageConsumer,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val messageProvider: BaseMessageProvider,
) : BaseStoreViewModel<HistoryNamespace.Event, HistoryNamespace.State, HistoryNamespace.Effect, HistoryNamespace.Command>(
    store = store,
    initialState = HistoryNamespace.State(),
    sharingStarted = SharingStarted.WhileSubscribed(5000),
    initialEvent = HistoryNamespace.Event.InitialLoad,
) {

    fun onPullDownRefreshed() {
        accept(HistoryNamespace.Event.Refresh)
    }

    fun onLoadMore() {
        accept(HistoryNamespace.Event.LoadMore)
    }

    fun onErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }
}
