package com.dezdeqness.data.datasource

import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.mapper.AnimeMapper
import com.dezdeqness.data.core.ApiException
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.RelatedItemMapper
import com.dezdeqness.data.mapper.RoleMapper
import com.dezdeqness.data.mapper.ScreenshotMapper
import com.dezdeqness.domain.model.AnimeBriefEntity
import com.dezdeqness.domain.model.AnimeChronologyEntity
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
                throw response.createApiException()
            }

        }

    override fun getDetailsAnimeMainInfo(id: Long, isAuthorized: Boolean) = tryWithCatch {
        val response = if (isAuthorized) {
            apiService.getDetailsAnimeMainInfoWithAuth(id = id).execute()
        } else {
            apiService.getDetailsAnimeMainInfo(id).execute()
        }

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val details = animeMapper.fromResponse(responseBody)
            Result.success(details)
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAnimeScreenshots(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeScreenshots(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val screenshots = responseBody.map(screenshotMapper::fromResponse)
            Result.success(screenshots)
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAnimeRelated(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeRelated(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val relates = responseBody.mapNotNull(relatedItemMapper::fromResponse)
            Result.success(relates)
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAnimeRoles(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeRoles(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val roles = roleMapper.fromResponse(responseBody)
            Result.success(roles)
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAnimeSimilar(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeSimilar(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val list = responseBody.map { item -> animeMapper.fromResponse(item) }
            Result.success(list)
        } else {
            throw response.createApiException()
        }

    }

    override fun getDetailsChronology(id: Long) = tryWithCatch {
        val response = apiService.getDetailsAnimeChronology(id = id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val list =
                responseBody.nodes?.map { item -> animeMapper.fromResponse(item) } ?: listOf()
            Result.success(list)
        } else {
            throw response.createApiException()
        }

    }

}
