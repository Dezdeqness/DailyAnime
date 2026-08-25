package com.dezdeqness.feature.calendar.data

import com.dezdeqness.contract.calendar.model.AnimeCalendarEntity
import com.dezdeqness.data.util.TimestampConverter
import com.dezdeqness.data.mapper.AnimeBriefMapper
import javax.inject.Inject

internal class CalendarMapper @Inject constructor(
    private val animeBriefMapper: AnimeBriefMapper,
    private val converter: TimestampConverter,
) {

    fun fromResponse(calendarRemote: AnimeCalendarRemote) =
        calendarRemote.anime?.let { animeBriefMapper.fromResponse(it) }?.let { animeEntity ->
            AnimeCalendarEntity(
                duration = calendarRemote.duration ?: 0,
                nextEpisode = calendarRemote.nextEpisode ?: 0,
                nextEpisodeAtTimestamp = converter.convertToTimeStampWithTime(calendarRemote.nextEpisodeAt),
                anime = animeEntity,
            )
        }
}
