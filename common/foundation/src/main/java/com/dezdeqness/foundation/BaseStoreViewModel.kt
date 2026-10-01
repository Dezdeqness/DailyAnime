package com.dezdeqness.foundation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.architecture.store.paging.PageFooter
import com.dezdeqness.architecture.store.paging.PagedContent
import com.dezdeqness.architecture.store.paging.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import money.vivid.elmslie.core.store.ElmStore

abstract class BaseStoreViewModel<Event : Any, State : Any, Effect : Any, Command : Any>(
    protected val store: ElmStore<Event, State, Effect, Command>,
    initialState: State,
    sharingStarted: SharingStarted = SharingStarted.Lazily,
    initialEvent: Event? = null,
) : ViewModel() {

    val state: StateFlow<State> = store
        .states
        .let { flow ->
            if (initialEvent != null) {
                flow.onStart { store.accept(initialEvent) }
            } else {
                flow
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = sharingStarted,
            initialValue = initialState,
        )

    val effects: Flow<Effect> = store.effects

    val uiEffects: SharedFlow<Effect> = store.effects
        .filter { !handleEffect(it) }
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    protected open suspend fun handleEffect(effect: Effect): Boolean = false

    protected fun accept(event: Event) {
        store.accept(event)
    }

    protected fun <Item : Any> mapPagedContent(paging: PagingState<Item, *, *>): PagedContent<Item> = when {
        paging.items.isNotEmpty() -> PagedContent.Items(
            items = paging.items,
            footer = when {
                paging.isLoading && !paging.isRefreshing -> PageFooter.Loading
                paging.error != null -> PageFooter.Failed
                paging.endReached -> PageFooter.End
                else -> PageFooter.CanLoadMore
            },
        )
        paging.isLoading -> PagedContent.Loading
        paging.error != null -> PagedContent.Error
        paging.endReached -> PagedContent.Empty
        else -> PagedContent.Loading
    }
}
