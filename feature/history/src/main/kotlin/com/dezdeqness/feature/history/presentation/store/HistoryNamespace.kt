package com.dezdeqness.feature.history.presentation.store

import com.dezdeqness.feature.history.presentation.models.HistoryModel

interface HistoryNamespace {
    data class State(
        val list: List<HistoryModel> = listOf(),
        val status: HistoryStatus = HistoryStatus.Initial,
        val isPullDownRefreshing: Boolean = false,
        val currentPage: Int = 1,
        val hasNextPage: Boolean = false,
    )

    sealed class Event {
        data object InitialLoad : Event()
        data object LoadMore : Event()
        data object Refresh : Event()
        data class OnPageLoaded(val list: List<HistoryModel>, val hasNextPage: Boolean) : Event()
        data class OnLoadMorePageLoaded(val list: List<HistoryModel>, val hasNextPage: Boolean) : Event()
        data class OnLoadMorePageError(val message: String, val error: Throwable) : Event()
        data class OnLoadPageError(val message: String, val error: Throwable) : Event()
    }

    sealed class Effect {
        data object Error : Effect()
    }

    sealed class Command {
        data class LoadPage(val page: Int, val isLoadMore: Boolean = false) : Command()
    }
}

enum class HistoryStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
