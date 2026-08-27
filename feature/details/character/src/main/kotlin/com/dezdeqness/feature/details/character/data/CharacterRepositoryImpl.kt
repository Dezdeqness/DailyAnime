package com.dezdeqness.feature.details.character.data

import com.dezdeqness.contract.character.repository.CharacterRepository
import javax.inject.Inject

internal class CharacterRepositoryImpl @Inject constructor(
    private val characterRemoteDataSource: CharacterRemoteDataSource,
) : CharacterRepository {
    override fun getCharacterDetailsById(id: Long) = characterRemoteDataSource.getCharacterDetailsById(id)
}
