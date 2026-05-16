package com.dezdeqness.feature.forum.presentation

interface ForumActions {
    fun onPullDownRefreshed()
    fun onForumSectionClicked(permalink: String, name: String)
    fun onHotTopicClicked(topicId: Long)
}
