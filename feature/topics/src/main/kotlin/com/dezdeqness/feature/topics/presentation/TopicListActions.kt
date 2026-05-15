package com.dezdeqness.feature.topics.presentation

interface TopicListActions {
    fun onPullDownRefreshed()
    fun onLoadMore()
    fun onItemClicked(topicId: Long)
}
