package com.dezdeqness.presentation.features.history


interface HistoryActions {
    fun onPullDownRefreshed()
    fun onLoadMore()
    fun onInitialLoad()
    fun onBackPressed()
}
