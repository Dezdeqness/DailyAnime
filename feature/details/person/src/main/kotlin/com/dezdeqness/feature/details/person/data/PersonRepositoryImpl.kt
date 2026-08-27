package com.dezdeqness.feature.details.person.data

import com.dezdeqness.contract.person.repository.PersonRepository
import javax.inject.Inject

internal class PersonRepositoryImpl @Inject constructor(
    private val personRemoteDataSource: PersonRemoteDataSource,
) : PersonRepository {

    override suspend fun getPersonDetailsById(id: Long) = personRemoteDataSource.getPersonDetailsById(id)
}
