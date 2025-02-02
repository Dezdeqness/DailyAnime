package com.dezdeqness.data.datasource

import com.apollographql.apollo.ApolloClient
import com.dezdeqness.data.HomeQuery
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createGraphqlException
import com.dezdeqness.data.mapper.AnimeMapper

import com.dezdeqness.data.type.OrderEnum
import com.dezdeqness.domain.model.HomeEntity
import javax.inject.Inject

class HomeRemoteDatasourceImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val animeMapper: AnimeMapper,
) : HomeRemoteDatasource, BaseDataSource() {

    override suspend fun getHomeSections(
        genreIds: List<String>,
        limit: Int,
        order: OrderEnum
    ) = tryWithCatchSuspend {
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

        if (data != null && response.hasErrors().not()) {
            val sectionCalendar = data
                .c
                .filter { it.homeAnime.nextEpisodeAt != null }
                .map { animeMapper.fromResponseCalendar(it.homeAnime) }
                .sortedByDescending { it.nextEpisodeTimestamp }
                .take(5)

            val sectionQ1 = data.q1.map { animeMapper.fromResponse(it.homeAnime) }
            val sectionQ2 = data.q2.map { animeMapper.fromResponse(it.homeAnime) }
            val sectionQ3 = data.q3.map { animeMapper.fromResponse(it.homeAnime) }

            Result.success(
                HomeEntity(
                    calendarSection = sectionCalendar,
                    genreSections = mapOf(
                        genreIds[0] to sectionQ1,
                        genreIds[1] to sectionQ2,
                        genreIds[2] to sectionQ3,
                    )
                )
            )
        } else {
            throw response.createGraphqlException()
        }
    }
}
