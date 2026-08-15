package com.dezdeqness.contract.person.model

import com.dezdeqness.contract.anime.model.ImageEntity

data class PersonDetailsEntity(
    val id: Long,
    val name: String,
    val russian: String,
    val japanese: String,
    val malId: Long?,
    val image: ImageEntity,
    val url: String,
    val website: String,
    val birthOn: String,
    val synonyms: List<String>,
    val roles: Set<PersonRole>,
)
