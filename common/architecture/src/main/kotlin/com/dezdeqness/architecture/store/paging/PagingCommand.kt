package com.dezdeqness.architecture.store.paging

sealed interface PagingCommand<out Params : Any> {

    data class LoadPage<Params : Any>(val params: Params) : PagingCommand<Params>
}
