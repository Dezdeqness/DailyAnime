package com.dezdeqness.domain.model

sealed class ErrorEntity(override val message: String) : Throwable(message) {

    data class UnknownErrorEntity(override val message: String) : ErrorEntity(message)

}
