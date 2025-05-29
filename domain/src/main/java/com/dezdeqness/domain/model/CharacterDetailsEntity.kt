package com.dezdeqness.domain.model

data class CharacterDetailsEntity(
   val id: Long,
   val name: String,
   val russian: String,
   val image: ImageEntity,
   val url: String,
   val description: String?,
   val descriptionHTML: String,
   val seyuList: List<CharacterEntity>,
   val animeList: List<AnimeBriefEntity>,
)
