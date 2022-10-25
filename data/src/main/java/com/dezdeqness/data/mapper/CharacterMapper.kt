package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.CharacterRemote
import com.dezdeqness.domain.model.CharacterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterMapper @Inject constructor() {

    fun fromResponse(characterRemote: CharacterRemote) =
        CharacterEntity(
            id = characterRemote.id,
            name = characterRemote.name,
            russian = characterRemote.russian,
            images = characterRemote.images,
            url = characterRemote.url,
        )

}
