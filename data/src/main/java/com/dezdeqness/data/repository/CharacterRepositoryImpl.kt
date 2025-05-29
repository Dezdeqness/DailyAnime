package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.CharacterRemoteDataSource
import com.dezdeqness.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterRemoteDataSource: CharacterRemoteDataSource
): CharacterRepository {
    override fun getCharacterDetailsById(id: Long) =
        characterRemoteDataSource.getCharacterDetailsById(id)
}
