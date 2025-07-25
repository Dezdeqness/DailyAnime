package com.dezdeqness.data.datasource

import com.dezdeqness.domain.model.CharacterDetailsEntity

interface CharacterRemoteDataSource {
    fun getCharacterDetailsById(id: Long): Result<CharacterDetailsEntity>
}
