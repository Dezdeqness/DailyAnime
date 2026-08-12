package com.dezdeqness.architecture.store.paging

fun interface PagingCommandFactory<Params : Any, out Command : Any> {

    fun create(params: Params): Command
}
