package com.dezdeqness.presentation.message

import com.dezdeqness.presentation.models.MessageEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class MessageConsumer @Inject constructor() {

    private val _messageState: MutableSharedFlow<MessageEvent> = MutableSharedFlow()
    val messageState: SharedFlow<MessageEvent> = _messageState

    suspend fun onSuccessMessage(text: String) {
        _messageState.emit(
            MessageEvent.SuccessMessageEvent(
                successText = text
            )
        )
    }

    suspend fun onErrorMessage(text: String) {
        _messageState.emit(
            MessageEvent.ErrorMessageEvent(
                errorText = text
            )
        )
    }

}