package com.dezdeqness.domain.model

import com.dezdeqness.contract.anime.model.AnimeBriefEntity

data class HomeEntity(
    val calendarSection: List<HomeCalendarEntity>,
    val genreSections: Map<String, List<AnimeBriefEntity>>,
)
