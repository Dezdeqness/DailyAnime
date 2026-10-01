package com.dezdeqness.feature.search.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.architecture.store.paging.PagingEvent
import com.dezdeqness.contract.filter.model.SearchSectionUiModel
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Effect
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.Event
import com.dezdeqness.feature.search.presentation.store.SearchNamespace.State
import com.dezdeqness.feature.search.presentation.store.SearchStore
import com.dezdeqness.foundation.BaseStoreViewModel
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import money.vivid.elmslie.core.store.ElmStore

class AnimeViewModel @Inject constructor(
    @SearchStore store: ElmStore<Any, State, Effect, Any>,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
) : BaseStoreViewModel<Any, State, Effect, Any>(
    store = store,
    initialState = State(),
    sharingStarted = SharingStarted.Eagerly,
    initialEvent = Event.Init,
) {

    val uiState: StateFlow<AnimeSearchState> = state
        .map(::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, AnimeSearchState())

    val scrollToTopRequests: Flow<Unit> = uiEffects
        .filterIsInstance<Effect.ScrollToTop>()
        .map { }

    fun onQueryChanged(query: String) = accept(Event.QueryChanged(query))

    fun onFilterChanged(filters: List<SearchSectionUiModel>) = accept(Event.FiltersChanged(filters))

    fun onPullDownRefreshed() = accept(Event.PullRefreshed)

    fun onLoadMore() = accept(PagingEvent.LoadMore)

    fun onFabClicked() = accept(Event.FilterClicked)

    override suspend fun handleEffect(effect: Effect): Boolean = when (effect) {
        Effect.ShowError -> {
            messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
            true
        }

        is Effect.OpenFilters -> false

        Effect.ScrollToTop -> false
    }

    private fun toUiState(state: State) = AnimeSearchState(
        content = mapPagedContent(state.paging),
        isRefreshing = state.paging.isRefreshing,
        filterButton = FilterButtonState.Shown(isApplied = state.filters.isNotEmpty()),
    )
}
