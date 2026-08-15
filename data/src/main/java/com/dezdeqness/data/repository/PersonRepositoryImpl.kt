package com.dezdeqness.data.repository

import com.dezdeqness.contract.person.repository.PersonRepository
import com.dezdeqness.data.datasource.PersonRemoteDataSource
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personRemoteDataSource: PersonRemoteDataSource,
) : PersonRepository {

    override suspend fun getPersonDetailsById(id: Long) = personRemoteDataSource.getPersonDetailsById(id)
}
