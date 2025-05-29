package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.CharacterDetailsEntity

interface CharacterRepository {
    fun getCharacterDetailsById(id: Long): Result<CharacterDetailsEntity>
}
