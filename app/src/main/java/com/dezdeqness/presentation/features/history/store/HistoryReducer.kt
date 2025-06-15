package com.dezdeqness.presentation.features.history.store

import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Command
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Command.*
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Effect
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.Event
import com.dezdeqness.presentation.features.history.store.HistoryNamespace.State
import money.vivid.elmslie.core.store.StateReducer

val historyReducer = object : StateReducer<Event, State, Effect, Command>() {
    override fun Result.reduce(event: Event) {
        when (event) {
            is Event.InitialLoad -> {
                state {
                    copy(
                        status = HistoryStatus.Loading,
                        currentPage = 1,
                    )
                }
                commands { +LoadPage(page = 1) }
            }

            is Event.LoadMore -> {
                val nextPage = state.currentPage + 1
                state { copy(currentPage = nextPage) }
                commands { +LoadPage(nextPage, isLoadMore = true) }
            }

            is Event.Refresh -> {
                state { copy(currentPage = 1) }
                commands { +LoadPage(page = 1) }
            }

            is Event.OnLoadMorePageLoaded -> {
                state {
                    val list = (state.list + event.list).toSet()
                    copy(
                        status = HistoryStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        list = list.toList(),
                    )
                }
            }

            is Event.OnLoadMorePageError -> {
                effects { +Effect.Error }
            }

            is Event.OnLoadPageError -> {
                state {
                    copy(status = HistoryStatus.Error)
                }
            }

            is Event.OnPageLoaded -> {
                state {
                    copy(
                        status = if (event.list.isEmpty()) HistoryStatus.Empty else HistoryStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        list = event.list.toList(),
                    )
                }
            }
        }
    }
}
