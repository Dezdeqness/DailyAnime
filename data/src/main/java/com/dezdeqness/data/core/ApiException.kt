package com.dezdeqness.data.core

class ApiException(
    val code: Int,
    override val message: String,
) : Throwable(message)
