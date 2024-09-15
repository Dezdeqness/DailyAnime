package com.dezdeqness.presentation.features.history

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.domain.usecases.GetHistoryUseCase
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val historyCompose: HistoryCompose,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: MessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded, BaseViewModel.Refreshable, BaseViewModel.LoadMore {

    private var currentPage = INITIAL_PAGE

    private val _historyStateFlow: MutableStateFlow<HistoryState> = MutableStateFlow(HistoryState())
    val historyStateFlow: StateFlow<HistoryState> get() = _historyStateFlow

    init {
        initialPageLoad()
    }

    override val viewModelTag = "HistoryViewModel"

    override fun setPullDownIndicatorVisible(isVisible: Boolean) {
        _historyStateFlow.value = _historyStateFlow.value.copy(
            isPullDownRefreshing = isVisible,
        )
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        _historyStateFlow.value = _historyStateFlow.value.copy(
            isInitialLoadingIndicatorShowing = isVisible,
        )
    }

    override fun setLoadMoreIndicator(isVisible: Boolean) {

    }

    override fun onPullDownRefreshed() {
        onPullDownRefreshed(
            action = {
                getHistoryUseCase.invoke(
                    pageNumber = INITIAL_PAGE,
                )
            },
            onSuccess = { state ->
                currentPage = state.currentPage

                val list = historyCompose.compose(state.list)

                _historyStateFlow.value = _historyStateFlow.value.copy(
                    list = list,
                    hasNextPage = state.hasNextPage,
                    isErrorStateShowing = false,
                )
            },
            onFailure = {
                logInfo("Error during pull down of history list", it)
            }
        )
    }

    fun onLoadMore() {
        onLoadMore(
            action = {
                getHistoryUseCase.invoke(
                    pageNumber = currentPage,
                )
            },
            onSuccess = { state ->
                currentPage = state.currentPage
                val hasNextPage = state.hasNextPage
                val list = historyCompose.compose(state.list)

                _historyStateFlow.value = _historyStateFlow.value.copy(
                    list = _historyStateFlow.value.list + list,
                    hasNextPage = hasNextPage,
                )
                hasNextPage
            },
            onFailure = {
                logInfo("Error during load more of history list", it)

                onErrorMessage()
            }
        )
    }

    private fun initialPageLoad() {
        onInitialLoad(
            action = {
                getHistoryUseCase.invoke(
                    pageNumber = INITIAL_PAGE,
                )
            },
            onSuccess = { state ->
                currentPage = state.currentPage
                val list = historyCompose.compose(state.list)

                _historyStateFlow.value = _historyStateFlow.value.copy(
                    list = list,
                    hasNextPage = state.hasNextPage,
                    isEmptyStateShowing = list.isEmpty(),
                    isErrorStateShowing = false,
                )
            },
            onFailure = {
                logInfo("Error during initial loading of state of history list", it)

                _historyStateFlow.value = _historyStateFlow.value.copy(
                    isErrorStateShowing = true,
                )
            }
        )
    }

    private fun onErrorMessage() {
        launchOnIo {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
        }
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }

}
