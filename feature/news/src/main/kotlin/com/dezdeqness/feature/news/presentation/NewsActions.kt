package com.dezdeqness.feature.news.presentation

interface NewsActions {
    fun onPullDownRefreshed()
    fun onLoadMore()
    fun onNewsItemClicked(topicId: Long)
}
