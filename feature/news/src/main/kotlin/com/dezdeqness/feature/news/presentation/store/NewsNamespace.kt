package com.dezdeqness.feature.news.presentation.store

import com.dezdeqness.feature.news.presentation.models.NewsUiModel

interface NewsNamespace {
    data class State(
        val list: List<NewsUiModel> = listOf(),
        val status: NewsStatus = NewsStatus.Initial,
        val isPullDownRefreshing: Boolean = false,
        val currentPage: Int = 1,
        val hasNextPage: Boolean = false,
    )

    sealed class Event {
        data object InitialLoad : Event()
        data object LoadMore : Event()
        data object Refresh : Event()
        data class OnPageLoaded(val list: List<NewsUiModel>, val hasNextPage: Boolean) : Event()
        data class OnLoadMorePageLoaded(val list: List<NewsUiModel>, val hasNextPage: Boolean) : Event()
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

enum class NewsStatus {
    Initial,
    Loading,
    Empty,
    Error,
    Loaded,
}
