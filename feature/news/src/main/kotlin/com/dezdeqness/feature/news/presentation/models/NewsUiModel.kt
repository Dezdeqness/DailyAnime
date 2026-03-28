package com.dezdeqness.feature.news.presentation.models

import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel

sealed class NewsUiModel {
    abstract fun id(): String

    data class NewsItem(val content: TopicPresentationModel) : NewsUiModel() {
        override fun id() = content.topicId.toString()
    }
}
