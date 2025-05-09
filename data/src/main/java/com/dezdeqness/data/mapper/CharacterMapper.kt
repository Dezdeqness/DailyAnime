package com.dezdeqness.data.mapper

import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.model.CharacterRemote
import com.dezdeqness.domain.model.CharacterEntity
import com.dezdeqness.domain.model.ImageEntity
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

    fun fromResponse(characterRemote: DetailsQuery.Character) =
        CharacterEntity(
            id = characterRemote.id.toLong(),
            name = characterRemote.name,
            russian = characterRemote.russian.orEmpty(),
            image = ImageEntity(
                preview = characterRemote.poster?.previewUrl.orEmpty(),
                original = characterRemote.poster?.originalUrl.orEmpty(),
            ),
            url = characterRemote.url,
        )

}
