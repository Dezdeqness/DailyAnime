package com.dezdeqness.feature.forum.presentation.store

import money.vivid.elmslie.core.store.StateReducer

val forumReducer = object :
    StateReducer<ForumNamespace.Event, ForumNamespace.State, ForumNamespace.Effect, ForumNamespace.Command>() {
    override fun Result.reduce(event: ForumNamespace.Event) {
        when (event) {
            is ForumNamespace.Event.InitialLoad -> {
                state {
                    state.copy(
                        status = ForumStatus.Loading,
                        hotTopicsStatus = HotTopicsStatus.Loading,
                    )
                }
                commands {
                    +ForumNamespace.Command.Load
                    +ForumNamespace.Command.LoadHotTopics
                }
            }

            is ForumNamespace.Event.Refresh -> {
                state {
                    state.copy(
                        isPullDownRefreshing = true,
                        hotTopicsStatus = HotTopicsStatus.Loading,
                    )
                }
                commands {
                    +ForumNamespace.Command.Load
                    +ForumNamespace.Command.LoadHotTopics
                }
            }

            is ForumNamespace.Event.OnLoaded -> {
                state {
                    state.copy(
                        status = if (event.list.isEmpty()) ForumStatus.Empty else ForumStatus.Loaded,
                        list = event.list,
                        isPullDownRefreshing = false,
                    )
                }
            }

            is ForumNamespace.Event.OnError -> {
                if (state.list.isNotEmpty()) {
                    state { state.copy(isPullDownRefreshing = false) }
                    effects { +ForumNamespace.Effect.Error }
                } else {
                    state {
                        state.copy(
                            status = ForumStatus.Error,
                            isPullDownRefreshing = false,
                        )
                    }
                }
            }

            is ForumNamespace.Event.OnHotTopicsLoaded -> {
                state {
                    state.copy(
                        hotTopics = event.topics,
                        hotTopicsStatus = HotTopicsStatus.Loaded,
                    )
                }
            }

            is ForumNamespace.Event.OnHotTopicsError -> {
                state {
                    state.copy(hotTopicsStatus = HotTopicsStatus.Error)
                }
            }
        }
    }
}
