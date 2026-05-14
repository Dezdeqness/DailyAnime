package com.dezdeqness.data.datasource

import com.dezdeqness.contract.topic.model.TopicEntity

interface TopicRemoteDataSource {

    fun getTopics(forumType: String, page: Int, limit: Int): Result<List<TopicEntity>>
    fun getHotTopics(limit: Int): Result<List<TopicEntity>>
    fun getTopicsById(id: Int): Result<TopicEntity>
}
