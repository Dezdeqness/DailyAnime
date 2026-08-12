package com.dezdeqness.architecture.store.paging

fun interface PageLoader<Item : Any, Params : Any, Error : Any> {

    suspend fun load(params: Params): PageResult<Item, Params, Error>
}
