package com.dezdeqness.presentation.models

import android.graphics.Color

sealed class MessageEvent(
    open val text: String,
    val textColor: Int,
    val backgroundColor: Int,
) {

    data class SuccessMessageEvent(
        val successText: String,
    ) : MessageEvent(
        text = successText,
        textColor = Color.WHITE,
        backgroundColor = Color.GREEN,
    )

    data class ErrorMessageEvent(
        val errorText: String,
    ) : MessageEvent(
        text = errorText,
        textColor = Color.WHITE,
        backgroundColor = Color.RED,
    )

}