package com.dezdeqness.feature.details.person.data.datasource

import com.dezdeqness.contract.person.model.PersonDetailsEntity

internal interface PersonRemoteDataSource {
    suspend fun getPersonDetailsById(id: Long): Result<PersonDetailsEntity>
}
