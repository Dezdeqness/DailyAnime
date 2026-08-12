package com.dezdeqness.architecture.store.paging

sealed interface PageResult<out Item : Any, out Params : Any, out Error : Any> {

    data class Success<Item : Any, Params : Any>(
        val items: List<Item>,
        val nextParams: Params?,
    ) : PageResult<Item, Params, Nothing>

    data class Failure<Error : Any>(val error: Error) : PageResult<Nothing, Nothing, Error>
}
