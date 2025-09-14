package com.dezdeqness.feature.history.presentation.store

import money.vivid.elmslie.core.store.StateReducer

val historyReducer = object :
    StateReducer<HistoryNamespace.Event, HistoryNamespace.State, HistoryNamespace.Effect, HistoryNamespace.Command>() {
    override fun Result.reduce(event: HistoryNamespace.Event) {
        when (event) {
            is HistoryNamespace.Event.InitialLoad -> {
                state {
                    HistoryNamespace.State(
                        status = HistoryStatus.Loading,
                        currentPage = 1,
                    )
                }
                commands { +HistoryNamespace.Command.LoadPage(page = 1) }
            }

            is HistoryNamespace.Event.LoadMore -> {
                val nextPage = state.currentPage + 1
                commands { +HistoryNamespace.Command.LoadPage(nextPage, isLoadMore = true) }
            }

            is HistoryNamespace.Event.Refresh -> {
                state { HistoryNamespace.State(currentPage = 1, isPullDownRefreshing = true) }
                commands { +HistoryNamespace.Command.LoadPage(page = 1) }
            }

            is HistoryNamespace.Event.OnLoadMorePageLoaded -> {
                state {
                    val list = (state.list + event.list).toSet()
                    HistoryNamespace.State(
                        status = HistoryStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        currentPage = state.currentPage + 1,
                        list = list.toList(),
                    )
                }
            }

            is HistoryNamespace.Event.OnLoadMorePageError -> {
                effects { +HistoryNamespace.Effect.Error }
            }

            is HistoryNamespace.Event.OnLoadPageError -> {
                state {
                    HistoryNamespace.State(
                        status = HistoryStatus.Error,
                        isPullDownRefreshing = false
                    )
                }
            }

            is HistoryNamespace.Event.OnPageLoaded -> {
                state {
                    HistoryNamespace.State(
                        status = if (event.list.isEmpty()) HistoryStatus.Empty else HistoryStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        list = event.list,
                        isPullDownRefreshing = false,
                    )
                }
            }
        }
    }
}
