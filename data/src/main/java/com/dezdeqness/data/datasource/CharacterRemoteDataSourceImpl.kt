package com.dezdeqness.data.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.dezdeqness.data.CharactersQuery
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createGraphqlException
import com.dezdeqness.data.mapper.CharacterMapper
import com.dezdeqness.domain.model.CharacterDetailsEntity
import javax.inject.Inject
import javax.inject.Named

class CharacterRemoteDataSourceImpl @Inject constructor(
    @Named("shikimori_graphql_client") private val apolloClient: ApolloClient,
    private val characterMapper: CharacterMapper,
) : CharacterRemoteDataSource, BaseDataSource() {

    override suspend fun getCharacterDetailsById(id: Long): Result<CharacterDetailsEntity> =
        tryWithCatchSuspend {
            val response = apolloClient
                .query(CharactersQuery(ids = Optional.present(listOf(id.toString()))))
                .execute()

            val data = response.data

            if (data != null && response.hasErrors().not() && data.characters.isNotEmpty()) {
                Result.success(characterMapper.fromResponse(data.characters.first()))
            } else {
                throw response.createGraphqlException()
            }
        }

}
