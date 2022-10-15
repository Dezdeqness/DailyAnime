package com.dezdeqness.core

class PaginationHelper(private val loadMoreCallback: (() -> Unit)? = null) {

    private var isPageLoading = false
    private var hasNextPage = false

    fun loadMore(currentPosition: Int, listSize: Int) {
        if (hasNextPage && !isPageLoading && (currentPosition >= (listSize * PAGINATION_LOAD_FACTOR))) {
            isPageLoading = true
            loadMoreCallback?.invoke()
        }
    }

    fun setLoadingState(hasNextPage: Boolean, isLoading: Boolean = false) {
        this.hasNextPage = hasNextPage
        this.isPageLoading = isLoading
    }

    companion object {
        private const val PAGINATION_LOAD_FACTOR = 0.75
    }

}
