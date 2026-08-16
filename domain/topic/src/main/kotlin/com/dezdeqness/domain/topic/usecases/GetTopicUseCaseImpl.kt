package com.dezdeqness.domain.topic.usecases

import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.contract.topic.usecases.GetTopicUseCase
import javax.inject.Inject

class GetTopicUseCaseImpl @Inject constructor(
    private val topicRepository: TopicRepository,
) : GetTopicUseCase {

    override fun invoke(forumType: String, pageNumber: Int): Result<GetTopicUseCase.NewsListState> {
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
            GetTopicUseCase.NewsListState(
                list = list,
                hasNextPage = hasNextPage,
                currentPage = if (list.isEmpty()) pageNumber else pageNumber + 1,
            ),
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
