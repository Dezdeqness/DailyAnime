package com.dezdeqness.data.datasource

import com.apollographql.apollo.ApolloClient
import com.dezdeqness.data.HomeQuery
import com.dezdeqness.data.mapper.AnimeMapper

import com.dezdeqness.data.type.OrderEnum
import com.dezdeqness.domain.model.GenreEntity
import com.dezdeqness.domain.model.HomeEntity
import javax.inject.Inject

class HomeRemoteDatasourceImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val animeMapper: AnimeMapper,
) : HomeRemoteDatasource {

    override suspend fun getHomeSections(
        genres: List<GenreEntity>,
        limit: Int,
        order: OrderEnum
    ): Result<HomeEntity> {
        val response = apolloClient.query(
            HomeQuery(
                genre1 = genres[0].id,
                genre2 = genres[1].id,
                genre3 = genres[2].id,
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
                    linkedMapOf(
                        genres[0] to sectionQ1,
                        genres[1] to sectionQ2,
                        genres[2] to sectionQ3,
                    )
                )
            )
        } else {
            Result.failure(response.exception?.cause ?: Throwable("error"))
        }
    }
}
