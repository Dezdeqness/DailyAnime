package com.dezdeqness.feature.topics.data

import com.dezdeqness.contract.topic.model.TopicEntity

internal interface TopicRemoteDataSource {

    fun getTopics(forumType: String, page: Int, limit: Int): Result<List<TopicEntity>>
    fun getHotTopics(limit: Int): Result<List<TopicEntity>>
    fun getTopicsById(id: Int): Result<TopicEntity>
}
