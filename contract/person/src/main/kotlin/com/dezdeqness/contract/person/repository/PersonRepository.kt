package com.dezdeqness.contract.person.repository

import com.dezdeqness.contract.person.model.PersonDetailsEntity

interface PersonRepository {
    suspend fun getPersonDetailsById(id: Long): Result<PersonDetailsEntity>
}
