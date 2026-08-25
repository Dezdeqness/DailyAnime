package com.dezdeqness.feature.topics.data

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import dagger.Lazy
import javax.inject.Inject

internal class TopicRemoteDataSourceImpl @Inject constructor(
    private val topicApiService: Lazy<TopicApiService>,
    private val topicMapper: TopicMapper,
) : TopicRemoteDataSource, BaseDataSource() {

    override fun getTopics(forumType: String, page: Int, limit: Int): Result<List<TopicEntity>> = tryWithCatch {
        val response = topicApiService.get().getTopics(forumType = forumType, page = page, limit = limit).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(responseBody.map(topicMapper::fromResponse))
        } else {
            throw response.createApiException()
        }
    }

    override fun getHotTopics(limit: Int): Result<List<TopicEntity>> = tryWithCatch {
        val response = topicApiService.get().getHotTopics(limit = limit).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(responseBody.map(topicMapper::fromResponse))
        } else {
            throw response.createApiException()
        }
    }

    override fun getTopicsById(id: Int): Result<TopicEntity> = tryWithCatch {
        val response = topicApiService.get().getTopic(id = id).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            val topic = topicMapper.fromResponse(responseBody)
            Result.success(topic)
        } else {
            throw response.createApiException()
        }
    }
}
