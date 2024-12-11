package com.dezdeqness.domain.model

data class HomeEntity(
    val sections: LinkedHashMap<GenreEntity, List<AnimeBriefEntity>>,
)
