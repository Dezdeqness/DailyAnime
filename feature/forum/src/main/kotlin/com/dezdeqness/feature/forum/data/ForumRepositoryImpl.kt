package com.dezdeqness.feature.forum.data

import com.dezdeqness.contract.forum.repository.ForumRepository
import javax.inject.Inject

internal class ForumRepositoryImpl @Inject constructor(
    private val forumRemoteDataSource: ForumRemoteDataSource,
) : ForumRepository {

    override fun getForums() = forumRemoteDataSource.getForums()
}
