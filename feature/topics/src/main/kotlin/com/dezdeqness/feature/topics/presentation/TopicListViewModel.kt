package com.dezdeqness.feature.topics.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.feature.topics.presentation.store.TopicListNamespace
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore

class TopicListViewModel @Inject constructor(
    store: ElmStore<
        TopicListNamespace.Event,
        TopicListNamespace.State,
        TopicListNamespace.Effect,
        TopicListNamespace.Command,
        >,
    private val messageConsumer: MessageConsumer,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val messageProvider: BaseMessageProvider,
) : BaseStoreViewModel<
    TopicListNamespace.Event,
    TopicListNamespace.State,
    TopicListNamespace.Effect,
    TopicListNamespace.Command,
    >(
    store = store,
    initialState = TopicListNamespace.State(),
    sharingStarted = SharingStarted.Lazily,
    initialEvent = TopicListNamespace.Event.InitialLoad,
) {

    fun onPullDownRefreshed() {
        accept(TopicListNamespace.Event.Refresh)
    }

    fun onLoadMore() {
        accept(TopicListNamespace.Event.LoadMore)
    }

    fun onErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }
}
