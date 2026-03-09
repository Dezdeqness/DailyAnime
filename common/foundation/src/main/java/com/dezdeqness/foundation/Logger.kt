package com.dezdeqness.foundation

interface Logger {

    fun logInfo(tag: String, message: String)

    fun logInfo(tag: String, message: String = "Error has happen", throwable: Throwable)
}
