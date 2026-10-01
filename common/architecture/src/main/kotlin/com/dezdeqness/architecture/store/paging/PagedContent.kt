package com.dezdeqness.architecture.store.paging

sealed interface PagedContent<out Item : Any> {
    data object Loading : PagedContent<Nothing>
    data object Empty : PagedContent<Nothing>
    data object Error : PagedContent<Nothing>
    data class Items<Item : Any>(
        val items: List<Item>,
        val footer: PageFooter,
    ) : PagedContent<Item>
}

enum class PageFooter {
    CanLoadMore,
    Loading,
    Failed,
    End,
}
