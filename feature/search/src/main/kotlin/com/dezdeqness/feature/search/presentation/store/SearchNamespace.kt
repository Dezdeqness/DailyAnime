package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.architecture.store.paging.HasPaging
import com.dezdeqness.architecture.store.paging.PagingState
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel

interface SearchNamespace {

    data class State(
        override val paging: PagingState<AnimeUiModel, SearchPageParams, SearchError> = PagingState(),
        val query: String = "",
        val filters: List<SearchSectionUiModel> = emptyList(),
        val isScrollToTopPending: Boolean = false,
    ) : HasPaging<AnimeUiModel, SearchPageParams, SearchError> {
        override fun updatePaging(paging: PagingState<AnimeUiModel, SearchPageParams, SearchError>): State =
            copy(paging = paging)
    }

    sealed interface Event {
        data object Init : Event
        data class QueryChanged(val query: String) : Event
        data class FiltersChanged(val filters: List<SearchSectionUiModel>) : Event
        data object PullRefreshed : Event
        data object AdultContentChanged : Event
        data object FilterClicked : Event
    }

    sealed interface Command {
        data object ObserveAdultContent : Command
    }

    sealed interface Effect {
        data object ShowError : Effect
        data object ScrollToTop : Effect
        data class OpenFilters(val filters: List<SearchSectionUiModel>) : Effect
    }
}

data class SearchPageParams(
    val page: Int,
    val params: AnimeSearchParams,
)

sealed interface SearchError {
    data object Network : SearchError
    data class Unknown(val cause: Throwable) : SearchError
}
