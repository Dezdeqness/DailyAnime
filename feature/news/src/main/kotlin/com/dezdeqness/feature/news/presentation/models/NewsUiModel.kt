package com.dezdeqness.feature.news.presentation.models

sealed class NewsUiModel {
    abstract fun id(): String

    data class NewsItem(
        val topicId: Long,
        val title: String,
        val userNickname: String,
        val userAvatarUrl: String,
        val date: String,
        val commentsCount: Long,
        val contentBlocks: List<ParagraphBlock>,
        val footerBlocks: List<ParagraphBlock>,
        val linkedTitle: String? = null,
        val linkedImageUrl: String? = null,
        val linkedId: Long? = null,
    ) : NewsUiModel() {
        override fun id() = topicId.toString()
    }
}
