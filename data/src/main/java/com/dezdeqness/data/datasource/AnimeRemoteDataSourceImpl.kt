package com.dezdeqness.data.datasource

import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.mapper.AnimeMapper
import com.dezdeqness.data.core.ApiException
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.mapper.RelatedItemMapper
import com.dezdeqness.data.mapper.RoleMapper
import com.dezdeqness.data.mapper.ScreenshotMapper
import javax.inject.Inject

class AnimeRemoteDataSourceImpl @Inject constructor(
    private val apiService: AnimeApiService,
    private val animeMapper: AnimeMapper,
    private val screenshotMapper: ScreenshotMapper,
    private val roleMapper: RoleMapper,
    private val relatedItemMapper: RelatedItemMapper,
) : BaseDataSource(), AnimeRemoteDataSource {

    override fun getListAnime(
        queryMap: Map<String, String>,
        pageNumber: Int,
        sizeOfPage: Int,
        searchQuery: String
    ) =
        tryWithCatch {
            val call = if (searchQuery.isEmpty()) {
                apiService.getListAnime(
                    limit = sizeOfPage,
                    page = pageNumber,
                    options = queryMap,
                )
            } else {
                apiService.getListAnimeWithSearchQuery(
                    limit = sizeOfPage,
                    page = pageNumber,
                    options = queryMap,
                    search = searchQuery,
                )
            }

            val response = call.execute()

            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                val list = responseBody.map { item -> animeMapper.fromResponse(item) }
                Result.success(list)
            } else {
                throw ApiException(response.code(), response.errorBody().toString())
            }

        }

    override fun getDetailsAnimeMainInfo(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeMainInfo(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val details = animeMapper.fromResponse(responseBody)
            Result.success(details)
        } else {
            throw ApiException(response.code(), response.errorBody().toString())
        }
    }

    override fun getDetailsAnimeScreenshots(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeScreenshots(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val screenshots = responseBody.map(screenshotMapper::fromResponse)
            Result.success(screenshots)
        } else {
            throw ApiException(response.code(), response.errorBody().toString())
        }
    }

    override fun getDetailsAnimeRelated(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeRelated(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val relates = responseBody.mapNotNull(relatedItemMapper::fromResponse)
            Result.success(relates)
        } else {
            throw ApiException(response.code(), response.errorBody().toString())
        }
    }

    override fun getDetailsAnimeRoles(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeRoles(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val roles = roleMapper.fromResponse(responseBody)
            Result.success(roles)
        } else {
            throw ApiException(response.code(), response.errorBody().toString())
        }
    }
}
