package com.dezdeqness.architecture.store.paging

data class PagingState<Item : Any, Params : Any, Error : Any>(
    val items: List<Item> = emptyList(),
    val nextParams: Params? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val error: Error? = null,
)
