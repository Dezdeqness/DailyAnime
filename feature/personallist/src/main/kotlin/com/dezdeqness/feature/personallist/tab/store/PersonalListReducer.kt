package com.dezdeqness.feature.personallist.tab.store

import money.vivid.elmslie.core.store.StateReducer

val personalListReducer = object :
    StateReducer<PersonalListNamespace.Event, PersonalListNamespace.State, PersonalListNamespace.Effect, PersonalListNamespace.Command>() {
    override fun Result.reduce(event: PersonalListNamespace.Event) {
        when (event) {
            is PersonalListNamespace.Event.InitialLoad -> {
                state {
                    PersonalListNamespace.State(
                        personalListStatus = event.status,
                        dataStatus = PersonalListStatus.Loading,
                        currentPage = 1,
                    )
                }
                commands {
                    +PersonalListNamespace.Command.LoadPage(
                        status = event.status, page = 1,
                    )
                }
            }

            is PersonalListNamespace.Event.LoadMore -> {
                if (!state.hasLocalChanges) {
                    val nextPage = state.currentPage + 1
                    commands {
                        +PersonalListNamespace.Command.LoadPage(
                            status = state.personalListStatus,
                            page = nextPage,
                            isLoadMore = true,
                        )
                    }
                }
            }

            is PersonalListNamespace.Event.Refresh -> {
                state { state.copy(currentPage = 1, isPullDownRefreshing = true) }
                commands {
                    +PersonalListNamespace.Command.LoadPage(
                        status = state.personalListStatus,
                        page = 1,
                    )
                }
            }

            is PersonalListNamespace.Event.OnLoadMorePageLoaded -> {
                state {
                    val list = (state.list + event.list).toSet()
                    state.copy(
                        dataStatus = PersonalListStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        currentPage = state.currentPage + 1,
                        list = list.toList(),
                    )
                }
            }

            is PersonalListNamespace.Event.OnLoadMorePageError -> {
                effects { +PersonalListNamespace.Effect.Error }
            }

            is PersonalListNamespace.Event.OnLoadPageError -> {
                state {
                    state.copy(
                        dataStatus = PersonalListStatus.Error,
                        isPullDownRefreshing = false
                    )
                }
            }

            is PersonalListNamespace.Event.OnPageLoaded -> {
                state {
                    state.copy(
                        dataStatus = if (event.list.isEmpty()) PersonalListStatus.Empty else PersonalListStatus.Loaded,
                        hasNextPage = event.hasNextPage,
                        list = event.list,
                        isPullDownRefreshing = false,
                        hasLocalChanges = false,
                        hasPendingRefresh = false,
                    )
                }
            }

            is PersonalListNamespace.Event.ItemRemovedLocally -> {
                state {
                    state.copy(
                        list = state.list.filter { it.id != event.userRateId },
                        hasLocalChanges = true,
                        hasNextPage = false,
                    )
                }
            }

            is PersonalListNamespace.Event.DismissRefreshHint -> {
                state {
                    state.copy(hasLocalChanges = false)
                }
                commands {
                    +PersonalListNamespace.Command.LoadPage(
                        status = state.personalListStatus,
                        page = 1,
                    )
                }
            }

            is PersonalListNamespace.Event.MarkPendingRefresh -> {
                state {
                    state.copy(hasPendingRefresh = true)
                }
            }

            is PersonalListNamespace.Event.CheckPendingRefresh -> {
                if (state.hasPendingRefresh) {
                    commands {
                        +PersonalListNamespace.Command.LoadPage(
                            status = state.personalListStatus,
                            page = 1,
                        )
                    }
                }
            }
        }
    }
}
