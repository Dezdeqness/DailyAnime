package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.architecture.store.FeatureReducer
import com.dezdeqness.architecture.store.paging.PagingEvent
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Effect
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.State
import javax.inject.Inject
import kotlin.reflect.KClass
import money.vivid.elmslie.core.store.dsl.ResultBuilder

class SearchPageLoadedReducer @Inject constructor() :
    FeatureReducer<PagingEvent.PageLoaded<AnimeUiModel, SearchPageParams>, State, Effect, Any>(
        @Suppress("UNCHECKED_CAST")
        (PagingEvent.PageLoaded::class as KClass<PagingEvent.PageLoaded<AnimeUiModel, SearchPageParams>>),
    ) {

    override fun ResultBuilder<State, Effect, Any>.reduce(
        event: PagingEvent.PageLoaded<AnimeUiModel, SearchPageParams>,
    ) {
        if (state.isScrollToTopPending) {
            state { copy(isScrollToTopPending = false) }
            effects { +Effect.ScrollToTop }
        }
    }
}

class SearchLoadFailedReducer @Inject constructor() :
    FeatureReducer<PagingEvent.LoadFailed<SearchError>, State, Effect, Any>(
        @Suppress("UNCHECKED_CAST")
        (PagingEvent.LoadFailed::class as KClass<PagingEvent.LoadFailed<SearchError>>),
    ) {

    override fun ResultBuilder<State, Effect, Any>.reduce(event: PagingEvent.LoadFailed<SearchError>) {
        state { copy(isScrollToTopPending = false) }
        if (state.paging.items.isNotEmpty()) {
            effects { +Effect.ShowError }
        }
    }
}
