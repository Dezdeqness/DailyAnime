package com.dezdeqness.data.core

open class AppException(
    override val message: String = "",
) : Throwable(message)
