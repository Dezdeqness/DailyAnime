package com.dezdeqness.feature.topics.presentation.store

import money.vivid.elmslie.core.store.StateReducer

val topicListReducer = object :
    StateReducer<TopicListNamespace.Event, TopicListNamespace.State, TopicListNamespace.Effect, TopicListNamespace.Command>() {
    override fun Result.reduce(event: TopicListNamespace.Event) {
        when (event) {
            is TopicListNamespace.Event.InitialLoad -> {
                state { TopicListNamespace.State(status = TopicListStatus.Loading, currentPage = 1) }
                commands { +TopicListNamespace.Command.LoadPage(page = 1) }
            }

            is TopicListNamespace.Event.LoadMore -> {
                val nextPage = state.currentPage + 1
                commands { +TopicListNamespace.Command.LoadPage(nextPage, isLoadMore = true) }
            }

            is TopicListNamespace.Event.Refresh -> {
                state { TopicListNamespace.State(currentPage = 1, isPullDownRefreshing = true) }
                commands { +TopicListNamespace.Command.LoadPage(page = 1) }
            }

            is TopicListNamespace.Event.OnLoadMorePageLoaded -> {
                state {
                    val list = (state.list + event.list).distinctBy { it.id() }
                    TopicListNamespace.State(
                        status = TopicListStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        currentPage = state.currentPage + 1,
                        list = list,
                    )
                }
            }

            is TopicListNamespace.Event.OnLoadMorePageError -> {
                effects { +TopicListNamespace.Effect.Error }
            }

            is TopicListNamespace.Event.OnLoadPageError -> {
                state {
                    TopicListNamespace.State(
                        status = TopicListStatus.Error,
                        isPullDownRefreshing = false,
                    )
                }
            }

            is TopicListNamespace.Event.OnPageLoaded -> {
                state {
                    TopicListNamespace.State(
                        status = if (event.list.isEmpty()) TopicListStatus.Empty else TopicListStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        list = event.list,
                        isPullDownRefreshing = false,
                    )
                }
            }
        }
    }
}
