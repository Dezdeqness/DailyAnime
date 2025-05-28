package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.CharacterDetailsEntity

interface CharacterRepository {
    suspend fun getCharacterDetailsById(id: Long): Result<CharacterDetailsEntity>
}
