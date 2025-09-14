package com.dezdeqness.core.message

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class MessageConsumer() {

    private val _messageState: Channel<MessageEvent> = Channel()
    val messageState: Flow<MessageEvent> = _messageState.receiveAsFlow()

    suspend fun onSuccessMessage(text: String) {
        _messageState.send(
            MessageEvent.SuccessMessageEvent(
                successText = text
            )
        )
    }

    suspend fun onErrorMessage(text: String) {
        _messageState.send(
            MessageEvent.ErrorMessageEvent(
                errorText = text
            )
        )
    }

}
