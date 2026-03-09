package com.dezdeqness.foundation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    protected val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    protected val logger: Logger,
) : ViewModel(), CoroutineScope {

    protected abstract val viewModelTag: String

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    protected fun logInfo(message: String) {
        logger.logInfo(tag = viewModelTag, message = message)
    }

    protected fun logInfo(message: String, throwable: Throwable) {
        logger.logInfo(tag = viewModelTag, message = message, throwable = throwable)
    }

    protected fun launchOnIo(lambda: suspend () -> Unit) =
        launch(coroutineDispatcherProvider.io()) {
            lambda.invoke()
        }

    protected fun launchOnMain(lambda: suspend () -> Unit) =
        launch(coroutineDispatcherProvider.main()) {
            lambda.invoke()
        }

    protected fun launchOnComputation(lambda: suspend () -> Unit) =
        launch(coroutineDispatcherProvider.computation()) {
            lambda.invoke()
        }

    protected fun <T> asyncOnIo(lambda: suspend () -> T) =
        async(coroutineDispatcherProvider.io()) {
            lambda.invoke()
        }

    protected fun <T> asyncOnMain(lambda: suspend () -> T) =
        async(coroutineDispatcherProvider.main()) {
            lambda.invoke()
        }

    protected fun <T> asyncOnComputation(lambda: suspend () -> T) =
        async(coroutineDispatcherProvider.computation()) {
            lambda.invoke()
        }
}
