package com.dezdeqness.data.datasource

import com.dezdeqness.contract.topic.model.TopicEntity
import com.dezdeqness.data.TopicApiService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.TopicMapper
import dagger.Lazy
import javax.inject.Inject

class TopicRemoteDataSourceImpl @Inject constructor(
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
}
