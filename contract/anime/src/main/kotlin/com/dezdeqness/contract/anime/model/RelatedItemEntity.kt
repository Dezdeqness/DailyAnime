package com.dezdeqness.contract.anime.model

data class RelatedItemEntity(
    val animeBriefEntity: AnimeBriefEntity,
    val relationTitle: String,
    val relationTitleRussian: String,
)
