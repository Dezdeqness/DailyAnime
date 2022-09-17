package com.dezdeqness.presentation.features.searchfilter.anime

import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeSearchFilter
import java.util.UUID

data class AnimeSearchFilterState(
    val items: List<AnimeSearchFilter> = listOf(),
    val currentCellUpdate: AnimeCell? = null,
    val listEvents: List<Event> = listOf(),
)

sealed class Event {
    val id: String = UUID.randomUUID().toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    data class ApplyFilter(val filters: List<AnimeSearchFilter> = listOf()) : Event()

}
