package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.architecture.store.FeatureReducer
import com.dezdeqness.architecture.store.paging.PagingCommand
import com.dezdeqness.architecture.store.paging.PagingState
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Command
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Effect
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Event
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.State
import javax.inject.Inject
import money.vivid.elmslie.core.store.dsl.ResultBuilder

class SearchReducer @Inject constructor() : FeatureReducer<Event, State, Effect, Any>(Event::class) {

    override fun ResultBuilder<State, Effect, Any>.reduce(event: Event) {
        when (event) {
            Event.Init -> {
                val params = firstPage(state.query, state.filters)
                state { copy(paging = PagingState(nextParams = params, isLoading = true)) }
                commands {
                    +PagingCommand.LoadPage(params)
                    +Command.ObserveAdultContent
                }
            }

            is Event.QueryChanged -> {
                if (event.query == state.query) return
                val params = firstPage(event.query, state.filters)
                state {
                    copy(
                        query = event.query,
                        paging = paging.restart(params),
                        isScrollToTopPending = true,
                    )
                }
                commands { +PagingCommand.LoadPage(params) }
            }

            is Event.FiltersChanged -> {
                val params = firstPage(state.query, event.filters)
                state {
                    copy(
                        filters = event.filters,
                        paging = paging.restart(params),
                        isScrollToTopPending = true,
                    )
                }
                commands { +PagingCommand.LoadPage(params) }
            }

            Event.PullRefreshed, Event.AdultContentChanged -> {
                val params = firstPage(state.query, state.filters)
                state { copy(paging = paging.restart(params)) }
                commands { +PagingCommand.LoadPage(params) }
            }

            Event.FilterClicked -> effects { +Effect.OpenFilters(state.filters) }
        }
    }

    private fun firstPage(query: String, filters: List<SearchSectionUiModel>) = SearchPageParams(
        page = FIRST_PAGE,
        params = AnimeSearchParams(text = query, filters = mapFilters(filters)),
    )

    private fun mapFilters(sections: List<SearchSectionUiModel>): Map<String, Set<String>> =
        sections
            .groupBy { it.queryId }
            .mapValues { (_, sameQuerySections) ->
                sameQuerySections.flatMapTo(linkedSetOf()) { section ->
                    section.items.map { it.id }.filter { it in section.selectedCells }
                }
            }
            .filterValues { it.isNotEmpty() }

    private companion object {
        const val FIRST_PAGE = 1
    }
}
