package com.dezdeqness.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.presentation.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    val appLogger: AppLogger,
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val refreshable: Refreshable
        get() {
            if (this !is Refreshable) {
                throw NotImplementedMethodException()
            }
            return this
        }


    private val initialLoaded: InitialLoaded
        get() {
            if (this !is InitialLoaded) {
                throw NotImplementedMethodException()
            }
            return this
        }

    private val loadMore: LoadMore
        get() {
            if (this !is LoadMore) {
                throw NotImplementedMethodException()
            }
            return this
        }

    abstract fun viewModelTag(): String

    abstract fun onEventConsumed(event: Event)

    protected fun <T> onPullDownRefreshed(
        action: () -> (Result<T>),
        onSuccess: (T) -> (Unit),
        onFailure: ((Throwable) -> (Unit))? = null,
        errorMessage: String = "",
    ) {
        makeRequest(
            action = action,
            onSuccess = onSuccess,
            onFailure = onFailure,
            errorMessage = errorMessage,
            isPullDownRefresh = true,
        )
    }

    protected fun <T> onPullDownRefreshed(
        collector: Flow<Result<T>>,
        onSuccess: suspend (T) -> (Unit),
        onFailure: ((Throwable) -> (Unit))? = null,
        errorMessage: String = "",
    ) {
        makeRequest(
            collector = collector,
            onSuccess = onSuccess,
            onFailure = onFailure,
            errorMessage = errorMessage,
            isPullDownRefresh = true,
        )
    }

    protected fun <T> onLoadMore(
        action: () -> (Result<T>),
        onSuccess: (T) -> (Boolean),
        onFailure: ((Throwable) -> (Unit))? = null,
        errorMessage: String = "",
    ) {
        makeRequest(
            action = action,
            onSuccess = onSuccess,
            onFailure = onFailure,
            errorMessage = errorMessage,
        )
    }

    protected fun <T> onInitialLoad(
        action: () -> (Result<T>),
        onSuccess: (T) -> (Unit),
        onFailure: ((Throwable) -> (Unit))? = null,
        errorMessage: String = "",
    ) {
        makeRequest(
            action = action,
            onSuccess = onSuccess,
            onFailure = onFailure,
            errorMessage = errorMessage,
            isInitialLoad = true,
        )
    }

    protected fun <T> onInitialLoad(
        collector: Flow<Result<T>>,
        onSuccess: suspend (T) -> (Unit),
        onFailure: ((Throwable) -> (Unit))? = null,
        errorMessage: String = "",
    ) {
        makeRequest(
            collector = collector,
            onSuccess = onSuccess,
            onFailure = onFailure,
            errorMessage = errorMessage,
            isInitialLoad = true,
        )
    }

    private fun <T> makeRequest(
        action: () -> (Result<T>),
        onSuccess: (T) -> (Unit),
        onFailure: ((Throwable) -> (Unit))? = null,
        isPullDownRefresh: Boolean = false,
        isInitialLoad: Boolean = false,
        errorMessage: String = "",
    ) {
        launchOnIo {
            if (isPullDownRefresh) {
                refreshable.setPullDownIndicatorVisible(isVisible = true)
            }
            if (isInitialLoad) {
                initialLoaded.setLoadingIndicatorVisible(isVisible = true)
            }
            action.invoke()
                .onSuccess { value ->
                    onSuccess.invoke(value)
                    if (isPullDownRefresh) {
                        refreshable.setPullDownIndicatorVisible(isVisible = false)
                    }
                    if (isInitialLoad) {
                        initialLoaded.setLoadingIndicatorVisible(isVisible = false)
                    }
                    onSuccess.invoke(value)
                }
                .onFailure { throwable ->
                    onFailure?.invoke(throwable)
                    if (isPullDownRefresh) {
                        refreshable.setPullDownIndicatorVisible(isVisible = false)
                    }
                    if (isInitialLoad) {
                        initialLoaded.setLoadingIndicatorVisible(isVisible = false)
                    }
                    appLogger.logInfo(
                        tag = viewModelTag(),
                        message = errorMessage,
                        throwable = throwable,
                    )
                }
        }
    }

    private fun <T> makeRequest(
        action: () -> (Result<T>),
        onSuccess: (T) -> (Boolean),
        onFailure: ((Throwable) -> (Unit))? = null,
        errorMessage: String = "",
    ) {
        launchOnIo {
            action.invoke()
                .onSuccess { value ->
                    onSuccess.invoke(value)
                    val hasNextPage = onSuccess.invoke(value)
                    loadMore.setLoadMoreIndicator(isVisible = hasNextPage)
                }
                .onFailure { throwable ->
                    onFailure?.invoke(throwable)
                    appLogger.logInfo(
                        tag = viewModelTag(),
                        message = errorMessage,
                        throwable = throwable,
                    )
                }
        }
    }

    private fun <T> makeRequest(
        collector: Flow<Result<T>>,
        onSuccess: suspend (T) -> (Unit),
        onFailure: ((Throwable) -> (Unit))? = null,
        isPullDownRefresh: Boolean = false,
        isInitialLoad: Boolean = false,
        isLoadMore: Boolean = false,
        errorMessage: String = "",
    ) {
        launchOnIo {
            if (isPullDownRefresh) {
                refreshable.setPullDownIndicatorVisible(isVisible = true)
            }
            if (isInitialLoad) {
                initialLoaded.setLoadingIndicatorVisible(isVisible = true)
            }
            collector.collect { flowValue ->
                flowValue
                    .onSuccess { value ->
                        onSuccess.invoke(value)
                        if (isPullDownRefresh) {
                            refreshable.setPullDownIndicatorVisible(isVisible = false)
                        }
                        if (isInitialLoad) {
                            initialLoaded.setLoadingIndicatorVisible(isVisible = false)
                        }
                    }
                    .onFailure { throwable ->
                        onFailure?.invoke(throwable)
                        if (isPullDownRefresh) {
                            refreshable.setPullDownIndicatorVisible(isVisible = false)
                        }
                        if (isInitialLoad) {
                            initialLoaded.setLoadingIndicatorVisible(isVisible = false)
                        }
                        if (isLoadMore) {
                            loadMore.setLoadMoreIndicator(isVisible = false)
                        }
                        appLogger.logInfo(
                            tag = viewModelTag(),
                            message = errorMessage,
                            throwable = throwable,
                        )
                    }
            }
        }
    }

    fun logInfo(message: String) {
        appLogger.logInfo(
            tag = viewModelTag(),
            message = message,
        )
    }

    fun logInfo(message: String, throwable: Throwable) {
        appLogger.logInfo(
            tag = viewModelTag(),
            message = message,
            throwable = throwable,
        )
    }

    fun launchOnIo(lambda: suspend () -> Unit) =
        launch(coroutineDispatcherProvider.io()) {
            lambda.invoke()
        }

    fun launchOnMain(lambda: () -> Unit) =
        launch(coroutineDispatcherProvider.main()) {
            lambda.invoke()
        }

    fun launchOnComputation(lambda: () -> Unit) =
        launch(coroutineDispatcherProvider.computation()) {
            lambda.invoke()
        }

    fun <T> asyncOnIo(lambda: () -> T) =
        async(coroutineDispatcherProvider.io()) {
            lambda.invoke()
        }

    fun <T> asyncOnMain(lambda: () -> T) =
        async(coroutineDispatcherProvider.main()) {
            lambda.invoke()
        }

    fun <T> asyncOnComputation(lambda: () -> T) =
        async(coroutineDispatcherProvider.computation()) {
            lambda.invoke()
        }

    interface Refreshable {
        fun onPullDownRefreshed()
        fun setPullDownIndicatorVisible(isVisible: Boolean)
    }

    interface InitialLoaded {
        fun setLoadingIndicatorVisible(isVisible: Boolean)
        class NotImplementedMethodException : Exception()
    }

    interface LoadMore {
        fun setLoadMoreIndicator(isVisible: Boolean)

    }

    class NotImplementedMethodException : Exception()

}
