package com.dezdeqness.feature.details.character.data

import com.dezdeqness.contract.character.model.CharacterDetailsEntity

internal interface CharacterRemoteDataSource {
    fun getCharacterDetailsById(id: Long): Result<CharacterDetailsEntity>
}
