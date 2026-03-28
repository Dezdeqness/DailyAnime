package com.dezdeqness.feature.news.presentation

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.feature.news.presentation.models.NewsUiModel
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer
import com.dezdeqness.shared.presentation.feature.topic.model.ParagraphBlock
import javax.inject.Inject

class NewsComposer @Inject constructor(
    private val topicPresentationComposer: TopicPresentationComposer,
) {

    fun compose(topics: List<TopicEntity>): List<NewsUiModel> {
        return topics.map { topic ->
            val content = topicPresentationComposer.compose(topic)

            NewsUiModel.NewsItem(
                content = content.copy(
                    footerBlocks = content.footerBlocks.filterIsInstance<ParagraphBlock.VideoContent>(),
                )
            )
        }
    }
}
