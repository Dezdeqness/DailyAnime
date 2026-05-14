package com.dezdeqness.domain.usecases

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.contract.topic.repository.TopicRepository

class GetHotTopicsUseCase(
    private val topicRepository: TopicRepository,
) {

    operator fun invoke(limit: Int = DEFAULT_LIMIT): Result<List<TopicEntity>> =
        topicRepository.getHotTopics(limit = limit)

    companion object {
        const val DEFAULT_LIMIT = 5
    }
}
