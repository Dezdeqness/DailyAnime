package com.dezdeqness.data.repository

import com.dezdeqness.contract.topic.repository.TopicRepository
import com.dezdeqness.data.datasource.TopicRemoteDataSource
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
    private val topicRemoteDataSource: TopicRemoteDataSource,
) : TopicRepository {

    override fun getTopicsByType(forumType: String, page: Int, limit: Int) =
        topicRemoteDataSource.getTopics(forumType = forumType, page = page, limit = limit)

    override fun getHotTopics(limit: Int) = topicRemoteDataSource.getHotTopics(limit = limit)

    override fun getTopicsById(id: Int) = topicRemoteDataSource.getTopicsById(id = id)
}
