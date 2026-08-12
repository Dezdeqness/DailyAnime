package com.dezdeqness.architecture.store.paging

import com.dezdeqness.architecture.store.FeatureReducer
import kotlin.reflect.KClass
import money.vivid.elmslie.core.store.dsl.ResultBuilder

class PagingReducer<Item : Any, Params : Any, Error : Any, State, Effect : Any, Command : Any>(
    private val commandFactory: PagingCommandFactory<Params, Command>,
) : FeatureReducer<PagingEvent<Item, Params, Error>, State, Effect, Command>(
    @Suppress("UNCHECKED_CAST") (PagingEvent::class as KClass<PagingEvent<Item, Params, Error>>),
)
    where State : HasPaging<Item, Params, Error>, State : Any {

    @Suppress("UNCHECKED_CAST")
    override fun ResultBuilder<State, Effect, Command>.reduce(event: PagingEvent<Item, Params, Error>) {
        val slice = state.paging
        when (event) {
            is PagingEvent.LoadFirst<*> -> {
                val params = event.initialParams as Params
                state {
                    state.updatePaging(
                        PagingState(nextParams = params, isLoading = true),
                    ) as State
                }
                commands { +commandFactory.create(params) }
            }

            is PagingEvent.Refresh<*> -> {
                if (slice.isRefreshing) return
                val params = event.initialParams as Params
                state {
                    state.updatePaging(
                        slice.copy(
                            nextParams = params,
                            isLoading = true,
                            isRefreshing = true,
                            error = null,
                        ),
                    ) as State
                }
                commands { +commandFactory.create(params) }
            }

            PagingEvent.LoadMore -> {
                val params = slice.nextParams ?: return
                if (slice.isLoading || slice.isRefreshing || slice.endReached) return
                state { state.updatePaging(slice.copy(isLoading = true)) as State }
                commands { +commandFactory.create(params) }
            }

            is PagingEvent.PageLoaded<*, *> -> {
                val items = event.items as List<Item>
                val nextParams = event.nextParams as Params?
                state {
                    state.updatePaging(
                        slice.copy(
                            items = if (slice.isRefreshing) items else slice.items + items,
                            nextParams = nextParams,
                            isLoading = false,
                            isRefreshing = false,
                            endReached = nextParams == null,
                        ),
                    ) as State
                }
            }

            is PagingEvent.LoadFailed<*> -> state {
                state.updatePaging(
                    slice.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = event.error as Error,
                    ),
                ) as State
            }
        }
    }
}
