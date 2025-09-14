package com.dezdeqness.data.mapper

import com.dezdeqness.data.DetailsQuery
import com.dezdeqness.data.model.CharacterDetailsRemote
import com.dezdeqness.data.model.CharacterRemote
import com.dezdeqness.domain.model.CharacterDetailsEntity
import com.dezdeqness.contract.anime.model.CharacterEntity
import com.dezdeqness.contract.anime.model.ImageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterMapper @Inject constructor(
    private val imageMapper: ImageMapper,
    private val animeMapper: AnimeMapper,
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

    fun fromResponse(character: CharacterDetailsRemote) =
        CharacterDetailsEntity(
            id = character.id.toLong(),
            name = character.name,
            russian = character.russian,
            image = imageMapper.fromResponse(character.image),
            url = character.url,
            description = character.description,
            descriptionHTML = character.descriptionHtml,
            seyuList = character.seyuList.map(::fromResponse),
            animeList = character.animeList.map(animeMapper::fromResponse),
        )

}
