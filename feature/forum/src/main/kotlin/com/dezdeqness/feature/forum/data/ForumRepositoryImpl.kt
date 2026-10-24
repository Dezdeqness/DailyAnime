package com.dezdeqness.feature.forum.data

import com.dezdeqness.contract.forum.repository.ForumRepository
import com.dezdeqness.data.core.config.ConfigManager
import javax.inject.Inject

internal class ForumRepositoryImpl @Inject constructor(
    private val forumRemoteDataSource: ForumRemoteDataSource,
    private val configManager: ConfigManager,
) : ForumRepository {

    override fun getForums() = forumRemoteDataSource.getForums().map { forums ->
        val excludedTopics = configManager.excludedTopics
        forums.filterNot { it.permalink in excludedTopics }
    }
}
