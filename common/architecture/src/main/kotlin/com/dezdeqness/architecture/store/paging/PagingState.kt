package com.dezdeqness.architecture.store.paging

data class PagingState<Item : Any, Params : Any, Error : Any>(
    val items: List<Item> = emptyList(),
    val nextParams: Params? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val error: Error? = null,
) {
    fun restart(params: Params): PagingState<Item, Params, Error> = copy(
        nextParams = params,
        isLoading = true,
        isRefreshing = true,
        endReached = false,
        error = null,
    )
}
