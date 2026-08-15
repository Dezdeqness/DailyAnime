package com.dezdeqness.contract.character.repository

import com.dezdeqness.contract.character.model.CharacterDetailsEntity

interface CharacterRepository {
    fun getCharacterDetailsById(id: Long): Result<CharacterDetailsEntity>
}
