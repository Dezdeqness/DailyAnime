package com.dezdeqness.feature.topicdetails.presentation

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.di.AssistedViewModelFactory
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore

object TopicIdKey : CreationExtras.Key<Long>

class TopicDetailsViewModel(
    store: ElmStore<
        TopicDetailsNamespace.Event,
        TopicDetailsNamespace.State,
        TopicDetailsNamespace.Effect,
        TopicDetailsNamespace.Command,
        >,
    private val messageConsumer: MessageConsumer,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val messageProvider: BaseMessageProvider,
    private val topicId: Long,
) : BaseStoreViewModel<
    TopicDetailsNamespace.Event,
    TopicDetailsNamespace.State,
    TopicDetailsNamespace.Effect,
    TopicDetailsNamespace.Command,
    >(
    store = store,
    initialState = TopicDetailsNamespace.State(),
    sharingStarted = SharingStarted.Lazily,
    initialEvent = TopicDetailsNamespace.Event.InitialLoad(topicId),
) {

    fun onPullDownRefreshed() {
        accept(TopicDetailsNamespace.Event.Refresh(topicId))
    }

    fun onErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }

    class Factory @Inject constructor(
        private val store: ElmStore<
            TopicDetailsNamespace.Event,
            TopicDetailsNamespace.State,
            TopicDetailsNamespace.Effect,
            TopicDetailsNamespace.Command,
            >,
        private val messageConsumer: MessageConsumer,
        private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
        private val messageProvider: BaseMessageProvider,
    ) : AssistedViewModelFactory<TopicDetailsViewModel> {

        override fun create(extras: CreationExtras): TopicDetailsViewModel {
            val topicId = extras[TopicIdKey] ?: 0L

            return TopicDetailsViewModel(
                store = store,
                messageConsumer = messageConsumer,
                coroutineDispatcherProvider = coroutineDispatcherProvider,
                messageProvider = messageProvider,
                topicId = topicId,
            )
        }
    }
}
