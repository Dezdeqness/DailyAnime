package com.dezdeqness.domain.model

import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.CharacterEntity
import com.dezdeqness.contract.anime.model.ImageEntity

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
