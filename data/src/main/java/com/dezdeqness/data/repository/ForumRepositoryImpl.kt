package com.dezdeqness.data.repository

import com.dezdeqness.contract.forum.repository.ForumRepository
import com.dezdeqness.data.datasource.ForumRemoteDataSource
import javax.inject.Inject

class ForumRepositoryImpl @Inject constructor(
    private val forumRemoteDataSource: ForumRemoteDataSource,
) : ForumRepository {

    override fun getForums() = forumRemoteDataSource.getForums()
}
