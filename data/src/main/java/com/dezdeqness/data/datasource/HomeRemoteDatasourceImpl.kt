package com.dezdeqness.data.datasource

import com.apollographql.apollo.ApolloClient
import com.dezdeqness.data.HomeQuery
import com.dezdeqness.data.mapper.AnimeMapper

import com.dezdeqness.data.type.OrderEnum
import com.dezdeqness.domain.model.HomeEntity
import javax.inject.Inject

class HomeRemoteDatasourceImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val animeMapper: AnimeMapper,
) : HomeRemoteDatasource {

    override suspend fun getHomeSections(
        genreIds: List<String>,
        limit: Int,
        order: OrderEnum
    ): Result<HomeEntity> {
        val response = apolloClient.query(
            HomeQuery(
                genre1 = genreIds[0],
                genre2 = genreIds[1],
                genre3 = genreIds[2],
                limit = limit,
                order = order
            )
        ).execute()

        val data = response.data

        return if (data != null && response.hasErrors().not()) {
            val sectionQ1 = data.q1.map { animeMapper.fromResponse(it.homeAnime) }
            val sectionQ2 = data.q2.map { animeMapper.fromResponse(it.homeAnime) }
            val sectionQ3 = data.q3.map { animeMapper.fromResponse(it.homeAnime) }

            Result.success(
                HomeEntity(
                    mapOf(
                        genreIds[0] to sectionQ1,
                        genreIds[1] to sectionQ2,
                        genreIds[2] to sectionQ3,
                    )
                )
            )
        } else {
            Result.failure(response.exception?.cause ?: Throwable("error"))
        }
    }
}
