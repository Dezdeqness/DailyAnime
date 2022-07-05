package com.dezdeqness.data.core

import com.dezdeqness.domain.ErrorMapper

abstract class BaseDataSource(
    private val errorMapper: ErrorMapper,
) {

    fun <T> tryWithCatch(block: () -> Result<T>) = try {
        block()
    } catch (exception: Throwable) {
        Result.failure(errorMapper.map(exception))
    }

}
