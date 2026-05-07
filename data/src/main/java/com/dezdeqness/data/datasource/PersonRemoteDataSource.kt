package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.PersonDetailsEntity

interface PersonRemoteDataSource {
    suspend fun getPersonDetailsById(id: Long): Result<PersonDetailsEntity>
}
