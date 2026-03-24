package com.dezdeqness.feature.news.presentation.store

import money.vivid.elmslie.core.store.StateReducer

val newsReducer = object :
    StateReducer<NewsNamespace.Event, NewsNamespace.State, NewsNamespace.Effect, NewsNamespace.Command>() {
    override fun Result.reduce(event: NewsNamespace.Event) {
        when (event) {
            is NewsNamespace.Event.InitialLoad -> {
                state { NewsNamespace.State(status = NewsStatus.Loading, currentPage = 1) }
                commands { +NewsNamespace.Command.LoadPage(page = 1) }
            }

            is NewsNamespace.Event.LoadMore -> {
                val nextPage = state.currentPage + 1
                commands { +NewsNamespace.Command.LoadPage(nextPage, isLoadMore = true) }
            }

            is NewsNamespace.Event.Refresh -> {
                state { NewsNamespace.State(currentPage = 1, isPullDownRefreshing = true) }
                commands { +NewsNamespace.Command.LoadPage(page = 1) }
            }

            is NewsNamespace.Event.OnLoadMorePageLoaded -> {
                state {
                    val list = (state.list + event.list).distinctBy { it.id() }
                    NewsNamespace.State(
                        status = NewsStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        currentPage = state.currentPage + 1,
                        list = list,
                    )
                }
            }

            is NewsNamespace.Event.OnLoadMorePageError -> {
                effects { +NewsNamespace.Effect.Error }
            }

            is NewsNamespace.Event.OnLoadPageError -> {
                state {
                    NewsNamespace.State(
                        status = NewsStatus.Error,
                        isPullDownRefreshing = false,
                    )
                }
            }

            is NewsNamespace.Event.OnPageLoaded -> {
                state {
                    NewsNamespace.State(
                        status = if (event.list.isEmpty()) NewsStatus.Empty else NewsStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        list = event.list,
                        isPullDownRefreshing = false,
                    )
                }
            }
        }
    }
}
