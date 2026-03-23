package com.dezdeqness.contract.topic.repository

import com.dezdeqness.contract.topic.model.TopicEntity

interface TopicRepository {

    fun getTopicsByType(forumType: String, page: Int, limit: Int): Result<List<TopicEntity>>

}
