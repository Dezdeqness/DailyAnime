package com.dezdeqness.di.source.unsupported

internal fun <T> unsupported(): Result<T> =
    Result.failure(UnsupportedOperationException("Not supported by the active source"))
