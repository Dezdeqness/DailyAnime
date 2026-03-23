package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.repository.TopicRepository

class GetTopicUseCase(
    private val topicRepository: TopicRepository,
) {

    operator fun invoke(forumType: String, pageNumber: Int): Result<NewsListState> {
        val result = topicRepository.getTopicsByType(
            forumType = forumType,
            page = pageNumber,
            limit = PAGE_SIZE,
        )
        result.onFailure {
            return Result.failure(it)
        }
        val list = result.getOrDefault(listOf())

        val hasNextPage = list.size >= PAGE_SIZE

        return Result.success(
            NewsListState(
                list = list,
                hasNextPage = hasNextPage,
                currentPage = if (list.isEmpty()) pageNumber else pageNumber + 1,
            )
        )
    }

    data class NewsListState(
        val list: List<TopicEntity> = listOf(),
        val hasNextPage: Boolean = false,
        val currentPage: Int = 0,
    )

    companion object {
        const val PAGE_SIZE = 20
    }
}
