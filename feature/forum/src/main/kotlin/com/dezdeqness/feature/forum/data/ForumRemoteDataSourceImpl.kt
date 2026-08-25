package com.dezdeqness.feature.forum.data

import com.dezdeqness.contract.forum.model.ForumEntity
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import dagger.Lazy
import javax.inject.Inject

internal class ForumRemoteDataSourceImpl @Inject constructor(
    private val forumApiService: Lazy<ForumApiService>,
    private val forumMapper: ForumMapper,
) : ForumRemoteDataSource, BaseDataSource() {

    override fun getForums(): Result<List<ForumEntity>> = tryWithCatch {
        val response = forumApiService.get().getForums().execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(responseBody.map(forumMapper::fromResponse))
        } else {
            throw response.createApiException()
        }
    }
}
