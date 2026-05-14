package com.dezdeqness.data.datasource

import com.dezdeqness.contract.forum.model.ForumEntity

interface ForumRemoteDataSource {

    fun getForums(): Result<List<ForumEntity>>
}
