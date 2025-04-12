package com.dezdeqness.data.datasource

import com.apollographql.apollo.ApolloClient
import com.dezdeqness.data.AnimeApiService
import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.mapper.AnimeMapper
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.core.createGraphqlException
import com.dezdeqness.data.mapper.RelatedItemMapper
import com.dezdeqness.data.mapper.RoleMapper
import com.dezdeqness.data.mapper.ScreenshotMapper
import com.dezdeqness.domain.DetailsAdditionalInfo
import dagger.Lazy
import javax.inject.Inject

class AnimeRemoteDataSourceImpl @Inject constructor(
    private val apiService: Lazy<AnimeApiService>,
    private val apolloClient: ApolloClient,
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
                apiService.get().getListAnime(
                    limit = sizeOfPage,
                    page = pageNumber,
                    options = queryMap,
                )
            } else {
                apiService.get().getListAnimeWithSearchQuery(
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
            apiService.get().getDetailsAnimeMainInfoWithAuth(id = id).execute()
        } else {
            apiService.get().getDetailsAnimeMainInfo(id).execute()
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
        val response = apiService.get().getDetailsAnimeScreenshots(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val screenshots = responseBody.map(screenshotMapper::fromResponse)
            Result.success(screenshots)
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAnimeRelated(id: Long) = tryWithCatch {
        val response = apiService.get().getDetailsAnimeRelated(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val relates = responseBody.mapNotNull(relatedItemMapper::fromResponse)
            Result.success(relates)
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAnimeRoles(id: Long) = tryWithCatch {
        val response = apiService.get().getDetailsAnimeRoles(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val roles = roleMapper.fromResponse(responseBody)
            Result.success(roles)
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAnimeSimilar(id: Long) = tryWithCatch {
        val response = apiService.get().getDetailsAnimeSimilar(id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val list = responseBody.map { item -> animeMapper.fromResponse(item) }
            Result.success(list)
        } else {
            throw response.createApiException()
        }

    }

    override fun getDetailsChronology(id: Long) = tryWithCatch {
        val response = apiService.get().getDetailsAnimeChronology(id = id).execute()

        val responseBody = response.body()

        if (response.isSuccessful && responseBody != null) {
            val list =
                responseBody.nodes?.map { item -> animeMapper.fromResponse(item) } ?: listOf()
            Result.success(list)
        } else {
            throw response.createApiException()
        }

    }

    override suspend fun getAdditionalInfo(id: Long): Result<DetailsAdditionalInfo> =
        tryWithCatchSuspend {
            val response = apolloClient.query(DetailsQuery(id.toString())).execute()

            val data = response.data

            if (data != null && response.hasErrors().not()) {
                val detailsData = data.animes.first()
                val screenshots = detailsData.screenshots.map(screenshotMapper::fromResponse)
                val related =
                    detailsData.related?.mapNotNull(relatedItemMapper::fromResponse).orEmpty()
                val characters = roleMapper.fromResponseGraphql(detailsData.characterRoles)

                Result.success(
                    Triple(screenshots, related, characters)
                )
            } else {
                throw response.createGraphqlException()
            }
        }

}
