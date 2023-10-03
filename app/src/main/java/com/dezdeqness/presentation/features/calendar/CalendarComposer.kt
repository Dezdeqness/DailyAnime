package com.dezdeqness.presentation.features.calendar

import com.dezdeqness.domain.model.AnimeCalendarEntity
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.CalendarListUiModel
import com.dezdeqness.presentation.models.CalendarUiModel
import com.dezdeqness.utils.ImageUrlUtils
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CalendarComposer @Inject constructor(
    private val imageUrlUtils: ImageUrlUtils,
) {

    private val dateFormatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())

    fun compose(items: List<AnimeCalendarEntity>, query: String): List<AdapterItem> {
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
        val uiItems = mutableListOf<AdapterItem>()

        map.entries.forEach { entry ->

            val calendarItems = mutableListOf<CalendarUiModel>()
            entry.value.forEach {
                calendarItems.add(
                    CalendarUiModel(
                        id = it.anime.id,
                        name = it.anime.russian,
                        episodeInfo = "${it.nextEpisode} эп.",
                        logoUrl = imageUrlUtils.getImageWithBaseUrl(it.anime.image.preview),
                    )
                )
            }

            uiItems.add(
                CalendarListUiModel(
                    header = entry.key,
                    items = calendarItems.sortedBy { it.name },
                )
            )
        }

        return uiItems
    }

}
