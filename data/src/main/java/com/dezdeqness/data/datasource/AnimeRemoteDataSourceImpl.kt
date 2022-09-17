package com.dezdeqness.data.datasource

import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.mapper.AnimeMapper
import com.dezdeqness.data.core.ApiException
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.domain.mapper.ErrorMapper
import javax.inject.Inject

class AnimeRemoteDataSourceImpl @Inject constructor(
    private val mangaApiService: AnimeApiService,
    private val animeMapper: AnimeMapper,
    errorMapper: ErrorMapper,
) : BaseDataSource(errorMapper), AnimeRemoteDataSource {

    override fun getListAnime(queryMap: Map<String, String>) = tryWithCatch {

        val response = mangaApiService.getListAnime(limit = 40, page = 1, options = queryMap).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val list = responseBody.map { item -> animeMapper.fromResponse(item) }
            Result.success(list)
        } else {
            throw ApiException(response.code(), response.errorBody().toString())
        }

    }
}
