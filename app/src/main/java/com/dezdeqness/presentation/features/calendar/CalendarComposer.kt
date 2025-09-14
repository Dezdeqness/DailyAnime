package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.data.utils.ImageUrlUtils
import com.dezdeqness.domain.model.AnimeCalendarEntity
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CalendarComposer @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
) {

    private val dateFormatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun compose(items: List<AnimeCalendarEntity>, query: String): List<CalendarListUiModel> {
        val filteredList = if (query.isEmpty()) items else items.filter {
            it.anime.russian.contains(query, ignoreCase = true)
        }
        val map = linkedMapOf<String, MutableList<AnimeCalendarEntity>>()
        filteredList.forEach { item ->
            val key = dateFormatter.format(item.nextEpisodeAtTimestamp)
            if (map.contains(key)) {
                map[key]?.add(item)
            } else {
                map[key] = mutableListOf(item)
            }
        }
        val uiItems = mutableListOf<CalendarListUiModel>()

        map.entries.forEach { entry ->

            val calendarItems = mutableListOf<CalendarUiModel>()
            entry.value.forEach {
                calendarItems.add(
                    CalendarUiModel(
                        id = it.anime.id,
                        name = it.anime.russian.ifEmpty { it.anime.name },
                        ongoingEpisode = it.nextEpisode,
                        logoUrl = imageUrlUtils.getImageWithBaseUrl(it.anime.image.preview),
                        type = it.anime.kind.name,
                        score = it.anime.score.toString(),
                        time = timeFormatter.format(it.nextEpisodeAtTimestamp)
                    )
                )
            }

            uiItems.add(
                CalendarListUiModel(
                    header = entry.key,
                    items = calendarItems,
                )
            )
        }

        return uiItems
    }

}
