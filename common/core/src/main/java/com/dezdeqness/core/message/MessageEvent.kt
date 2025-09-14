package com.dezdeqness.core.message

sealed class MessageEvent(
    open val text: String,
    val status: MessageEventStatus,
) {

    data class SuccessMessageEvent(
        val successText: String,
    ) : MessageEvent(
        text = successText,
        status = MessageEventStatus.Success,
    )

    data class ErrorMessageEvent(
        val errorText: String,
    ) : MessageEvent(
        text = errorText,
        status = MessageEventStatus.Error,
    )

    enum class MessageEventStatus {
        Success,
        Error,
        Unknown;
    }
}
