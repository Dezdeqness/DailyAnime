package com.dezdeqness.feature.topics.presentation

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.feature.topics.presentation.models.TopicListUiModel
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import com.dezdeqness.shared.presentation.feature.topic.model.ParagraphBlock
import javax.inject.Inject

class TopicListComposer @Inject constructor(
    private val topicPresentationComposer: TopicPresentationComposer,
) {

    fun compose(topics: List<TopicEntity>): List<TopicListUiModel> {
        return topics.map { topic ->
            val content = topicPresentationComposer.compose(topic)

            TopicListUiModel.Item(
                content = content.copy(
                    footerBlocks = content.footerBlocks.filterIsInstance<ParagraphBlock.VideoContent>(),
                ),
            )
        }
    }
}
