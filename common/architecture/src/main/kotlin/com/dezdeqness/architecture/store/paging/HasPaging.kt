package com.dezdeqness.architecture.store.paging

interface HasPaging<Item : Any, Params : Any, Error : Any> {

    val paging: PagingState<Item, Params, Error>

    fun updatePaging(paging: PagingState<Item, Params, Error>): Any
}
