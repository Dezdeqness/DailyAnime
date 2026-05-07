package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.PersonDetailsEntity

interface PersonRepository {
    suspend fun getPersonDetailsById(id: Long): Result<PersonDetailsEntity>
}
