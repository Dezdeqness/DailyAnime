package com.dezdeqness.presentation.event

import com.dezdeqness.presentation.features.animedetails.AnimeStatsTransferModel
import com.dezdeqness.presentation.features.editrate.EditRateUiModel
import com.dezdeqness.presentation.models.AnimeSearchFilter
import java.util.*
import kotlin.collections.ArrayList

sealed class Event {
    val id: String = UUID.randomUUID().toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

sealed class ConsumableEvent : Event()

data class AnimeDetails(val animeId: Long) : ConsumableEvent()

data class OpenVideo(val url: String) : ConsumableEvent()

data class ApplyFilter(val filters: List<AnimeSearchFilter> = listOf()) : Event()

data class NavigateToFilter(val filters: List<AnimeSearchFilter> = listOf()) : Event()

data class NavigateToEditRate(val rateId: Long) : Event()

data class EditUserRate(val userRateUiModel: EditRateUiModel) : Event()

data class NavigateToScreenshotViewer(val currentIndex: Int, val screenshots: ArrayList<String>) : Event()

data class PagerScrollToPage(val index: Int) : Event()

object ScrollToTop : Event()

object NavigateToPersonalList : Event()

object NavigateToUnauthorized : Event()

object NavigateToProfile : Event()

data class SwitchDarkTheme(val isEnabled: Boolean) : Event()

data class ShareUrl(val url: String) : ConsumableEvent()

data class NavigateToAnimeState(
    val scoreList: List<AnimeStatsTransferModel>,
    val statusesList: List<AnimeStatsTransferModel>,
) : Event()

data class NavigateToSimilar(val animeId: Long) : Event()

data class NavigateToChronology(val animeId: Long) : Event()

data class OpenMenuPopupFilter(val sort: String) : Event()

object CloseAuthorization: Event()

object AuthorizationSuccess : Event()
