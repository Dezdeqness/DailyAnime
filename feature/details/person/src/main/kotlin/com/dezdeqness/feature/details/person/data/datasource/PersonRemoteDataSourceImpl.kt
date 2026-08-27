package com.dezdeqness.feature.details.person.data.datasource

import com.apollographql.apollo.ApolloClient
import com.dezdeqness.data.PersonQuery
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createGraphqlException
import com.dezdeqness.data.mapper.PersonMapper
import javax.inject.Inject
import javax.inject.Named

internal class PersonRemoteDataSourceImpl @Inject constructor(
    @Named("shikimori_graphql_client") private val apolloClient: ApolloClient,
    private val personMapper: PersonMapper,
) : BaseDataSource(), PersonRemoteDataSource {

    override suspend fun getPersonDetailsById(id: Long) = tryWithCatchSuspend {
        val response = apolloClient.query(PersonQuery(id.toString())).execute()

        val data = response.data

        if (data != null && response.hasErrors().not()) {
            val person = data.people.first()
            Result.success(personMapper.fromResponse(person))
        } else {
            throw response.createGraphqlException()
        }
    }
}
