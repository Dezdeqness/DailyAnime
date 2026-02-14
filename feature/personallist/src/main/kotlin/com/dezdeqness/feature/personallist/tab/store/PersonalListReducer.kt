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
                val nextPage = state.currentPage + 1
                commands {
                    +PersonalListNamespace.Command.LoadPage(
                        status = state.personalListStatus,
                        page = nextPage,
                        isLoadMore = true,
                    )
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
                    )
                }
            }

            is PersonalListNamespace.Event.ItemRemovedLocally -> {
                state {
                    state.copy(
                        list = state.list.filter { it.rateId != event.userRateId },
                    )
                }
            }

            is PersonalListNamespace.Event.UserRateChanged -> {
                val userRate = event.userRate
                if (userRate != null) {
                    commands {
                        +PersonalListNamespace.Command.UpdateUserRate(
                            userRate,
                            event.statusId
                        )
                    }
                }
            }

            PersonalListNamespace.Event.EditUserRateError -> {
                effects { +PersonalListNamespace.Effect.Error }
            }

            PersonalListNamespace.Event.EditUserRateSuccess -> {
                effects { +PersonalListNamespace.Effect.EditUserRateSuccess }
            }

            is PersonalListNamespace.Event.UpdateUserRateLocally -> {
                state {
                    var isSortNeed = false
                    val updatedList = state.list.map {
                        if (it.rateId == event.userRate.rateId) {
                            isSortNeed = true
                            it.copy(
                                episodes = event.userRate.episodes,
                                score = event.userRate.score,
                                updatedAtTimestamp = event.userRate.updatedAtTimestamp,
                            )
                        } else {
                            it
                        }
                    }

                    val finalList = if (isSortNeed) {
                        updatedList.sortedByDescending { it.updatedAtTimestamp }
                    } else {
                        updatedList
                    }

                    state.copy(
                        list = finalList
                    )
                }
            }

            is PersonalListNamespace.Event.UserRateIncrement -> {
                commands {
                    +PersonalListNamespace.Command.IncrementUserRate(
                        event.userRateId,
                        event.statusId,
                    )
                }

            }
        }
    }
}
