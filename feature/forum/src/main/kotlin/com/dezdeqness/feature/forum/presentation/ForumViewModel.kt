package com.dezdeqness.feature.forum.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore

class ForumViewModel @Inject constructor(
    store: ElmStore<
        ForumNamespace.Event,
        ForumNamespace.State,
        ForumNamespace.Effect,
        ForumNamespace.Command,
        >,
    private val messageConsumer: MessageConsumer,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val messageProvider: BaseMessageProvider,
) : BaseStoreViewModel<ForumNamespace.Event, ForumNamespace.State, ForumNamespace.Effect, ForumNamespace.Command>(
    store = store,
    initialState = ForumNamespace.State(),
    sharingStarted = SharingStarted.Lazily,
    initialEvent = ForumNamespace.Event.InitialLoad,
) {

    fun onPullDownRefreshed() {
        accept(ForumNamespace.Event.Refresh)
    }

    fun onErrorMessage() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }
}
