package com.dezdeqness.contract.topic.usecases

import com.dezdeqness.contract.topic.model.TopicEntity

interface GetTopicUseCase {

    operator fun invoke(forumType: String, pageNumber: Int): Result<GetTopicUseCase.NewsListState>

    data class NewsListState(
        val list: List<TopicEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )
}
