package com.dezdeqness.domain.model

data class HomeEntity(
    val calendarSection: List<HomeCalendarEntity>,
    val genreSections: Map<String, List<AnimeBriefEntity>>,
)
