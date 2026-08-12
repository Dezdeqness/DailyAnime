package com.dezdeqness.architecture.store.paging

sealed interface PagingEvent<out Item : Any, out Params : Any, out Error : Any> {

    data class LoadFirst<Params : Any>(
        val initialParams: Params,
    ) : PagingEvent<Nothing, Params, Nothing>

    data object LoadMore : PagingEvent<Nothing, Nothing, Nothing>

    data class Refresh<Params : Any>(
        val initialParams: Params,
    ) : PagingEvent<Nothing, Params, Nothing>

    data class PageLoaded<Item : Any, Params : Any>(
        val items: List<Item>,
        val nextParams: Params?,
    ) : PagingEvent<Item, Params, Nothing>

    data class LoadFailed<Error : Any>(val error: Error) : PagingEvent<Nothing, Nothing, Error>
}
