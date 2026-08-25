package com.dezdeqness.feature.forum.data

import com.dezdeqness.contract.forum.model.ForumEntity

internal interface ForumRemoteDataSource {

    fun getForums(): Result<List<ForumEntity>>
}
