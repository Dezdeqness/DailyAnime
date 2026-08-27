package com.dezdeqness.data.mapper

import com.dezdeqness.contract.anime.model.ImageEntity
import com.dezdeqness.contract.person.model.PersonDetailsEntity
import com.dezdeqness.contract.person.model.PersonRole
import com.dezdeqness.data.PersonQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonMapper @Inject constructor() {

    fun fromResponse(item: PersonQuery.Person) = PersonDetailsEntity(
        id = item.id.toLong(),
        name = item.name,
        russian = item.russian.orEmpty(),
        japanese = item.japanese.orEmpty(),
        malId = item.malId?.toLongOrNull(),
        image = ImageEntity(
            preview = item.poster?.previewUrl.orEmpty(),
            original = item.poster?.originalUrl.orEmpty(),
        ),
        url = item.url,
        website = item.website.orEmpty(),
        birthOn = item.birthOn?.date?.toString().orEmpty(),
        synonyms = item.synonyms,
        roles = buildRoles(
            isMangaka = item.isMangaka,
            isProducer = item.isProducer,
            isSeyu = item.isSeyu,
        ),
    )

    private fun buildRoles(isMangaka: Boolean, isProducer: Boolean, isSeyu: Boolean): Set<PersonRole> = buildSet {
        if (isMangaka) add(PersonRole.MANGAKA)
        if (isProducer) add(PersonRole.PRODUCER)
        if (isSeyu) add(PersonRole.SEYU)
    }
}
