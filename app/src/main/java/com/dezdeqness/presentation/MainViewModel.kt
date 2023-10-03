package com.dezdeqness.presentation

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.message.MessageConsumer
import javax.inject.Inject

class MainViewModel @Inject constructor(
    messageConsumer: MessageConsumer,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    val messageState = messageConsumer.messageState

    override val viewModelTag = "MainViewModel"

    override fun onEventConsumed(event: Event) {}
}