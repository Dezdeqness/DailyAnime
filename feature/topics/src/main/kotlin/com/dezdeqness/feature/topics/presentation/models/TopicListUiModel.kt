package com.dezdeqness.feature.topics.presentation.models

import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel

sealed class TopicListUiModel {
    abstract fun id(): String

    data class Item(val content: TopicPresentationModel) : TopicListUiModel() {
        override fun id() = content.topicId.toString()
    }
}
