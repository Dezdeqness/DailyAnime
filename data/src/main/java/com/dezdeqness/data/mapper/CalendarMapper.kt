package com.dezdeqness.data.mapper

import com.dezdeqness.data.model.AnimeCalendarRemote
import com.dezdeqness.domain.model.AnimeCalendarEntity
import javax.inject.Inject

class CalendarMapper @Inject constructor(
    private val animeMapper: AnimeMapper,
) {

    fun fromResponse(calendarRemote: AnimeCalendarRemote) =
        calendarRemote.anime?.let { animeMapper.fromResponse(it) }?.let { animeEntity ->
            AnimeCalendarEntity(
                duration = calendarRemote.duration ?: 0,
                nextEpisode = calendarRemote.nextEpisode ?: 0,
                nextEpisodeAt = calendarRemote.nextEpisodeAt.orEmpty(),
                anime = animeEntity
            )
        }

}