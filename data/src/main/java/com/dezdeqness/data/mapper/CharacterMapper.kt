package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.CharacterRemote
import com.dezdeqness.domain.model.CharacterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterMapper @Inject constructor(
    private val imageMapper: ImageMapper,
) {

    fun fromResponse(characterRemote: CharacterRemote) =
        CharacterEntity(
            id = characterRemote.id,
            name = characterRemote.name,
            russian = characterRemote.russian,
            image = imageMapper.fromResponse(characterRemote.image),
            url = characterRemote.url,
        )

}
