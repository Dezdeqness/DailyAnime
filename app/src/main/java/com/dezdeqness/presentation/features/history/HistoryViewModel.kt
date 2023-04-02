package com.dezdeqness.presentation.features.history

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.usecases.GetHistoryUseCase
import com.dezdeqness.presentation.event.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val historyCompose: HistoryCompose,
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

    override fun viewModelTag() = "HistoryViewModel"

    override fun onEventConsumed(event: Event) {

    }

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
                )
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
                )
            },
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }

}
