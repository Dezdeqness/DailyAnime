package com.dezdeqness.feature.news.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.feature.news.presentation.store.NewsNamespace
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    store: ElmStore<NewsNamespace.Event, NewsNamespace.State, NewsNamespace.Effect, NewsNamespace.Command>,
    private val messageConsumer: MessageConsumer,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val messageProvider: BaseMessageProvider,
) : BaseStoreViewModel<NewsNamespace.Event, NewsNamespace.State, NewsNamespace.Effect, NewsNamespace.Command>(
    store = store,
    initialState = NewsNamespace.State(),
    sharingStarted = SharingStarted.Lazily,
    initialEvent = NewsNamespace.Event.InitialLoad,
) {

    fun onPullDownRefreshed() {
        accept(NewsNamespace.Event.Refresh)
    }

    fun onLoadMore() {
        accept(NewsNamespace.Event.LoadMore)
    }

    fun onErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }
}
