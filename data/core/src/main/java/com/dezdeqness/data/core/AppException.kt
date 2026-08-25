package com.dezdeqness.data.core

// TODO: Refactor
open class AppException(
    override val message: String = "",
) : Throwable(message)
