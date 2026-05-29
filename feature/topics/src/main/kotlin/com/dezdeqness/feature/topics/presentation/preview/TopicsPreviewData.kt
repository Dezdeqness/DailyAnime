package com.dezdeqness.feature.topics.presentation.preview

import com.dezdeqness.feature.topics.presentation.models.TopicListUiModel
import com.dezdeqness.shared.presentation.feature.topic.preview.TopicPreviewData

object TopicsPreviewData {

    val list = TopicPreviewData.topicListItems.map { TopicListUiModel.Item(it) }
}
